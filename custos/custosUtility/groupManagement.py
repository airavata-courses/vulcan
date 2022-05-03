from custosUtility.custosBase import custos
from google.protobuf.json_format import MessageToJson
from custos.clients.group_management_client import GroupManagementClient
import json

class GroupManagement(custos):
    def __init__(self):
        custos.__init__(self)
    
        # instantiate a group management client
        self.group_management_client = GroupManagementClient(self.settings)
        self.created_groups = {}
    
    '''
    * Create groups
    * Admin group
    * Read only admin group
    * Gateway users group
    '''
    def create_group(self, group):
        try:
            print("Creating group: " + group['name'])

            grResponse = self.group_management_client.create_group(
                token = self.b64_encoded_custos_token,
                name = group['name'],
                description = group['description'],
                owner_id = group['owner_id'])
            resp = MessageToJson(grResponse)

            print(resp)

            respData = json.loads(resp)

            print("Created group id of " +
                group['name'] + ": " + respData['id'])

            self.created_groups[respData['name']] = respData['id']

        except Exception as e:
            print(f'Group creation failed!: Exception- {e}')

    '''
    * Alocate users to groups
    * Admin : alice, audery
    * Read only admin : sophia,abelota
    * Gateway User :  abgaill, adalee
    '''
    def add_users_to_groups(self, usr_map):
        try:
            group_id = self.created_groups[usr_map['group_name']]
            print("Assigning user " +
                usr_map['username'] + " to group " + usr_map['group_name'])
            val = self.group_management_client.add_user_to_group(
                token= self.b64_encoded_custos_token,
                username=usr_map['username'],
                group_id=group_id,
                membership_type='Member')
            resp = MessageToJson(val)
            print(resp)
            return resp

        except Exception as e:
            print(f"User allocation error: Exception- {e}")
    
    '''
    * Create group hierarchy
    * Assign Admin group as a child of Read Only Admin group
    '''
    def add_child_group_to_parent_group(self, gr_map):
        try:
            child_id = self.created_groups[gr_map['child_name']]
            parent_id = self.created_groups[gr_map['parent_name']]
            print("Assigning child group " +
                    gr_map['child_name'] + " to parent group " + gr_map['parent_name'])
            self.group_management_client.add_child_group(
                token= self.b64_encoded_custos_token,
                parent_group_id=parent_id,
                child_group_id=child_id)
        except Exception as e:
            print(f"Child group allocation error: Exception- {e}")
    
    '''
    removes child group from the parent group
    '''
    def remove_child_group(self, gr_map):

        try:
            child_id = self.created_groups[gr_map['child_name']]
            parent_id = self.created_groups[gr_map['parent_name']]

            return self.group_management_client.add_child_group(
                token = self.b64_encoded_custos_token, 
                parent_group_id = parent_id,
                child_group_id = child_id)
        except Exception as e:
            print(f'remove child failed: Exception - {e}')
