from custosUtility.custosBase import custos
from google.protobuf.json_format import MessageToJson
from custos.clients.resource_secret_management_client import ResourceSecretManagementClient

class SecretManagement(custos):
    def __init__(self):
        custos.__init__(self)
        
        # instantiate a resource management client
        self.resource_management_client = ResourceSecretManagementClient(self.settings)

    def generateSSH_key(self, request):
        try:
            return self.resource_management_client.add_ssh_credential(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                owner_id = request['owner_id'],
                description = request['description'])
        except Exception as e:
            print(f'generate SSH key failed: Exception - {e}')


    def getSSHKey(self, ssh_credential_token):
        try:
            return self.resource_management_client.get_ssh_credential(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                ssh_credential_token=ssh_credential_token)
        except Exception as e:
            print(f'get SSH key failed: Exception - {e}')


    def addPasswordCredential(self, request):
        try: 
            return self.resource_management_client.add_password_credential(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                owner_id = request['owner_id'],
                description = request['description'],
                password = request['password'])
        except Exception as e:
            print(f'addPasswordCredential failed: Exception - {e}')

    def getPasswordCredential(self, password_credential_token):
        try:
            return self.resource_management_client.get_password_credential(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                password_credential_token = password_credential_token)
        except Exception as e:
            print(f'getPasswordCredential failed: Exception - {e}')
