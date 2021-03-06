import json
from custosUtility.custosBase import custos
from google.protobuf.json_format import MessageToJson
from custos.clients.identity_management_client import IdentityManagementClient


class IdentityManagement(custos):
    def __init__(self):
        custos.__init__(self)

        # instantiate a identity management client
        self.identity_management_client = IdentityManagementClient(
            self.settings)

    def login(self, request):

        try:
            message = self.identity_management_client.authenticate(
                token=self.b64_encoded_custos_token,
                username=request['username'],
                password=request['password'])
            response = MessageToJson(message)
            return json.loads(response)

        except Exception as e:
            print(f'Login failed: Exception - {e}')

    def obtain_access_token_from_code(self, redirect_uri, code):
        try:
            return self.identity_management_client.token(token=self.b64_encoded_custos_token, redirect_uri=redirect_uri, code=code,
                                                         grant_type="authorization_code")
        except Exception as e:
            print(f'Obtain access token failed: Exception - {e}')

    def get_oidc_configuration(self, client_id):
        try:
            return self.identity_management_client.get_oidc_configuration(token=self.b64_encoded_custos_token, client_id=client_id)
        except Exception as e:
            print(f'Get OIDC config failed: Exception - {e}')

    def logout(self, request):
        try:
            operation = self.identity_management_client.end_user_session(
                token=self.b64_encoded_custos_token, refresh_token=request['refresh_token'])
            if operation.status:
                return "Success"
            else:
                return "Unable to logout."
        except Exception as e:
            print(f'Logout failed: Exception - {e}')
