import custos.clients.utils.utilities as utl
from custos.transport.settings import CustosServerClientSettings

'''
* CUSTOS base class
* instantiate the server details and token
'''
class custos:
    def __init__(self):
        # read settings
        self.settings = CustosServerClientSettings(
                custos_host='custos.scigap.org',
                custos_port='31499', 
                custos_client_id='custos-nupxzghtrypha1kq7jio-10003418',
                custos_client_sec='ilB0UCn83JiKT1UBaPrMbNtsuUYmQz0twD5VIFgx')
        
        # obtain base 64 encoded token for tenant
        self.b64_encoded_custos_token = utl.get_token(custos_settings= self.settings)


    