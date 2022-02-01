from flask import Flask, request
from nexrad import NexRad

app = Flask(__name__)

@app.route('/')
def index():
    return "server started"

@app.route('/saveurl', methods=['POST'])
def saveurl():
    data = request.get_json()

    date = data['date']
    month = data['month']
    year = data['year']
    time = data['time']
    station = data['station']

    if data != None:
        app.config.from_pyfile('config.py')
        nexRad = NexRad(app.config["GRPC_SERVER"], app.config["GRPC_SERVER_PORT"],  date, month, year, station, time)
        
        if(nexRad.validate()):
            print(nexRad.saveNexradData())
            return "success"

    return "fail"

if __name__ == "__main__":
    app.run(debug=True)