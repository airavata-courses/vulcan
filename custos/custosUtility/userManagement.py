import json
from custosUtility.custosBase import custos
from google.protobuf.json_format import MessageToJson
from custos.clients.user_management_client import UserManagementClient


class UserManagement(custos):
    def __init__(self):
        custos.__init__(self)

        # instantiate a user management client
        self.user_management_client = UserManagementClient(self.settings)

    '''
    Checks if the given user is in the database and
    Updates the user information in the database
    '''

    def updateUser(self, user):
        try:
            response = self.user_management_client.get_user(
                token=self.b64_encoded_custos_token,
                username=user['username'])

            if response == None:
                print('Invalid User details')
            else:
                self.user_management_client.update_user_profile(
                    token=self.b64_encoded_custos_token,
                    username=user['username'],
                    first_name=user['first_name'],
                    last_name=user['last_name'],
                    email=user['email'])

        except Exception as e:
            print(f'Update user profile failed: Exception - {e}')

        print('User profile updated successfully')

    '''
    Registers the given user with custos
    '''

    def registerUser(self, user):
        retVal = False
        try:
            self.user_management_client.register_user(
                token=self.b64_encoded_custos_token,
                username=user['username'],
                first_name=user['first_name'],
                last_name=user['last_name'],
                password=user['password'],
                email=user['email'],
                is_temp_password=False)

            self.user_management_client.enable_user(
                token=self.b64_encoded_custos_token,
                username=user['username'])

            print('user registration successful')
            retVal = True
        except Exception as e:
            print(f'User Registration failed: Exception- {e}')

        return retVal

    def enableUser(self, user):
        try:
            return self.user_management_client.enable_user(
                token=self.b64_encoded_custos_token,
                username=user['username'])
        except Exception as e:
            print(f'Enable user failed!: Exception- {e}')

    def getUser(self, user):
        try:
            user_rep = self.user_management_client.get_user(
                token=self.b64_encoded_custos_token,
                username=user['username'])
            user_rep = MessageToJson(user_rep)
            return json.loads(user_rep)
        except Exception as e:
            print(f'Invalid user!: Exception- {e}')

    def find_users(self, user):
        try:
            return self.user_management_client.find_users(
                token=self.b64_encoded_custos_token, offset=0, limit=1,
                username=user['username'])
        except Exception as e:
            print(f'Find users Failed: Exception- {e}')
