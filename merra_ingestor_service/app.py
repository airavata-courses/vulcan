import logging
import os
import sys
from sys import stdout
import json
from unicodedata import name
import urllib3
import certifi
import io
from typing import List
import requests
from time import sleep
from fastapi import FastAPI
import xarray as xr
from models import MerraClientRequest
import matplotlib.pyplot as plt
import uvicorn
from fastapi.responses import StreamingResponse
from threading import Thread


logger = logging.getLogger()
log_fhandler = logging.FileHandler('ingestor.log', mode='a')
log_shandler = logging.StreamHandler(stdout)
formatter = logging.Formatter(
    '%(levelname)s %(asctime)s - %(message)s')
log_shandler.setFormatter(formatter)
log_fhandler.setFormatter(formatter)
logger.addHandler(log_fhandler)
logger.addHandler(log_shandler)

app = FastAPI()
# Create a urllib PoolManager instance to make requests.
http = urllib3.PoolManager(cert_reqs='CERT_REQUIRED',ca_certs=certifi.where())
# Set the URL for the GES DISC subset service endpoint
url = 'https://disc.gsfc.nasa.gov/service/subset/jsonwsp'

# This method POSTs formatted JSON WSP requests to the GES DISC endpoint URL
# It is created for convenience since this task will be repeated more than once
def get_http_data(request):
    hdrs = {'Content-Type': 'application/json',
            'Accept'      : 'application/json'}
    data = json.dumps(request)       
    r = http.request('POST', url, body=data, headers=hdrs)
    response = json.loads(r.data)   
    # Check for errors
    if response['type'] == 'jsonwsp/fault' :
        print('API Error: faulty %s request' % response['methodname'])
        sys.exit(1)
    return response

def create_subset_request(begTime, endTime):
    # Create the subset request
    product = 'M2I3NPASM_5.12.4'
    varNames =['T', 'RH', 'O3','OMEGA']
    dimName = 'lev'
    dimVals = [1,4,7,13,17,19,21,22,23,24,25,26,27,29,30,31,32,33,35,36,37]
    # Construct the list of dimension name:value pairs to specify the desired subset
    dimSlice = []
    for i in range(len(dimVals)):
        dimSlice.append({'dimensionId': dimName, 'dimensionValue': dimVals[i]})
    subset_request = {
    'methodname': 'subset',
    'type': 'jsonwsp/request',
    'version': '1.0',
    'args': {
        'role'  : 'subset',
        'start' : begTime,
        'end'   : endTime,
        'box'   : [-180, -90, 180, -45],
        'crop'  : True, 
        'data': [{'datasetId': product,
                  'variable' : varNames[0],
                  'slice': dimSlice
                 },
                 {'datasetId': product,
                  'variable' : varNames[1],
                  'slice'    : dimSlice
                 },
                 {'datasetId': product,
                  'variable' : varNames[2],
                  'slice': dimSlice
                 }]
            }
        }
    return subset_request

def get_subset_data(start_time, end_time):
    # Submit the subset request to the GES DISC Server
    response = get_http_data(create_subset_request(start_time, end_time))
    # Report the JobID and initial status
    myJobId = response['result']['jobId']
    print('Job ID: '+myJobId)
    print('Job status: '+response['result']['Status'])
    status_request = {
    'methodname': 'GetStatus',
    'version': '1.0',
    'type': 'jsonwsp/request',
    'args': {'jobId': myJobId}
    }

# Check on the job status after a brief nap
    while response['result']['Status'] in ['Accepted', 'Running']:
        sleep(1)
        response = get_http_data(status_request)
        status  = response['result']['Status']
        percent = response['result']['PercentCompleted']
        print ('Job status: %s (%d%c complete)' % (status,percent,'%'))
    if response['result']['Status'] == 'Succeeded' :
        print ('Job Finished:  %s' % response['result']['message'])
    else : 
        print('Job Failed: %s' % response['fault']['code'])
        sys.exit(1)


    # Retrieve a plain-text list of results in a single shot using the saved JobID
    batchsize = 20
    results_request = {
        'methodname': 'GetResult',
        'version': '1.0',
        'type': 'jsonwsp/request',
        'args': {
            'jobId': myJobId,
            'count': batchsize,
            'startIndex': 0
        }
    }

    # Retrieve the results in JSON in multiple batches 
    # Initialize variables, then submit the first GetResults request
    # Add the results from this batch to the list and increment the count
    results = []
    count = 0 
    response = get_http_data(results_request) 
    count = count + response['result']['itemsPerPage']
    results.extend(response['result']['items']) 

    # Increment the startIndex and keep asking for more results until we have them all
    total = response['result']['totalResults']
    while count < total :
        results_request['args']['startIndex'] += batchsize 
        response = get_http_data(results_request) 
        count = count + response['result']['itemsPerPage']
        results.extend(response['result']['items'])
        
    # Check on the bookkeeping
    print('Retrieved %d out of %d expected items' % (len(results), total))
        # Sort the results into documents and URLs

    docs = []
    urls = []
    for item in results:
        try:
            if item['start'] and item['end'] : urls.append(item) 
        except:
            docs.append(item)
    return urls

#Download datasets from the GES DISC server using urls
def download_data(urls):
    # Use the requests library to submit the HTTP_Services URLs and write out the results.
    print('\nHTTP_services output:')
    files = []
    for item in urls :
        URL = item['link']
        #Check if the file exists in the local directory, if yes keep it, if not download it
        if os.path.isfile('./netCDF/' + item['label']):
            print('File %s already exists' % item['label'])
            files.append(item['label'])
            continue 
        result = requests.get(URL)
        try:
            result.raise_for_status()
            outfn = item['label']
            files.append(outfn)
            f = open(f'./netcdf/{outfn}','wb')
            f.write(result.content)
            f.close()
            print(outfn, "is downloaded")
        except:
            print('Error! Status code is %d for this URL:\n%s' % (result.status.code,URL))
            print('Help for downloading data is at https://disc.gsfc.nasa.gov/data-access')
            
    print('Downloading is done and find the downloaded files in your current working directory')
    return files

def do_plot(file, parameter):
    ds = xr.open_dataset('./netCDF/'+file)
    if parameter == 'T':  
        figu = ds.T.isel(lev = 20).mean(dim='time')
    elif parameter == 'RH':
        figu = ds.RH.isel(lev = 20).mean(dim='time')
    elif parameter == 'O3':
        figu = ds.O3.isel(lev = 20).mean(dim='time')
    f  = plt.subplots(figsize = (8,4))
    im = plt.imshow(figu, alpha=0.5)
    plt.axis('off')
    # figu.plot()
    bytes_image = io.BytesIO()
    plt.savefig(bytes_image, format='png', transparent=True)
    bytes_image.seek(0)
    return bytes_image

def convert_files(files):
    for file in files:
        ds = xr.open_dataset(f'./netCDF/{file}')
        # check if zarr file exists
        if os.path.isfile(f'./zarr/{file}.zarr'):
            logger.info(f'File {file}.zarr already exists')
            continue
        # ds.to_zarr('./zarr/'+file.replace('.nc','.zarr'))
        ds.close()
    logger.info('Converted files to zarr format')


@app.post('/')
async def serve(request: MerraClientRequest) -> List[float]:
    try:
        logger.debug(f'Incoming request: {request}')
        urls_ = get_subset_data(request.startdate, request.enddate)
        files = download_data(urls_)
        if files :
            try:
                bytes_image = do_plot(files[0], request.parameter)
                thread = Thread(target=convert_files(files))
                thread.start()
                return StreamingResponse(content=bytes_image, media_type='image/png')
            except ValueError as e:
                print(e)
        else:
            print('No files are downloaded or there is no data in the time range')
            sys.exit(1) 
        
    except Exception as e:
        logger.error(e)
        return e

if __name__ == "__main__":
    uvicorn.run(app, host='0.0.0.0', port=8080)


    
    

