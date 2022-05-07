from custosUtility.custosBase import custos
from google.protobuf.json_format import MessageToJson
from custos.clients.sharing_management_client import SharingManagementClient

class ShareManagement(custos):
    def __init__(self):
        custos.__init__(self)
        
        # instantiate a sharing management client
        self.share_management_client = SharingManagementClient(self.settings)
    
    def create_permission_type(self, id, name, description):
        try:
            return self.share_management_client.create_permission_type(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                id = id,
                name = name,
                description = description)
        except Exception as e:
            print(f'create_permission_type failed! Exception - {e}')

    def create_entity_type(self, id, name, description):
        try:
            return self.share_management_client.create_entity_type(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                id = id,
                name = name,
                description = description)
        except Exception as e:
            print(f'create_entity_type failed! Exception - {e}')

    def create_entity(self, id, name, description, owner_id, type):
        try:
            return self.share_management_client.create_entity(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                id = id,
                name = name,
                description = description,
                owner_id = owner_id,
                type = type,
                parent_id = '')
        except Exception as e:
            print(f'create_entity failed! Exception - {e}')

    def share_entity_with_user(self, entity_id, permission_type, user_id):
        try:
            return self.share_management_client.share_entity_with_users(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                entity_id=entity_id,
                permission_type=permission_type,
                user_id=user_id)
        except Exception as e:
            print(f'share_entity_with_user failed! Exception - {e}')


    def share_entity_with_group(self, entity_id, permission_type, group_id):
        try:
            return self.share_management_client.share_entity_with_groups(
                token= self.b64_encoded_custos_token,
                client_id= self.settings.CUSTOS_CLIENT_ID,
                entity_id=entity_id,
                permission_type=permission_type,
                group_id=group_id)
        except Exception as e:
            print(f'share_entity_with_group failed! Exception - {e}')


    def check_user_has_access(self, entity_id, permission_type, user_id):
        try:
            response = self.share_management_client.user_has_access(
                token = self.b64_encoded_custos_token,
                client_id = self.settings.CUSTOS_CLIENT_ID,
                entity_id = entity_id,
                permission_type = permission_type,
                user_id = user_id)
        except Exception as e:
            print(f'check_user_has_access failed! Exception - {e}')