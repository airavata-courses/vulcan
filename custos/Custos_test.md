
# Install Custos python SDK

pip install --index-url https://test.pypi.org/simple/ --extra-index-url https://pypi.org/simple custos-sdk==1.0.18

\#Setup Custos clients

-   User management client
-   Group management client
-   Resource secret management client
-   Sharing management client
-   Identity management client

``` {.python}
import os
import json
import random, string

from custos.clients.user_management_client import UserManagementClient
from custos.clients.group_management_client import GroupManagementClient
from custos.clients.resource_secret_management_client import ResourceSecretManagementClient
from custos.clients.sharing_management_client import SharingManagementClient
from custos.clients.identity_management_client import IdentityManagementClient


from custos.transport.settings import CustosServerClientSettings
import custos.clients.utils.utilities as utl

from google.protobuf.json_format import MessageToJson
try :
# read settings
  custos_settings = CustosServerClientSettings(custos_host='custos.scigap.org',
                 custos_port='31499', 
                 custos_client_id='custos-nupxzghtrypha1kq7jio-10003418',
                  custos_client_sec='ilB0UCn83JiKT1UBaPrMbNtsuUYmQz0twD5VIFgx')

# create custos user management client
  user_management_client = UserManagementClient(custos_settings)

# create custos group management client
  group_management_client = GroupManagementClient(custos_settings)

# create custos resource secret client
  resource_secret_client = ResourceSecretManagementClient(custos_settings)

# create sharing management client
  sharing_management_client = SharingManagementClient(custos_settings)

# create identity management client
  identity_management_client = IdentityManagementClient(custos_settings)


# obtain base 64 encoded token for tenant
  b64_encoded_custos_token = utl.get_token(custos_settings=custos_settings)
  print(b64_encoded_custos_token)

  created_groups = {}

  admin_user_name = "aarvind@iu.edu"
  admin_password = "Akshat the incarnate"
   
  resource_ids = []
  print("Successfully configured all custos clients")
except Exception as e:
  raise e
  print("Custos Id and Secret may wrong "+ str(e))

def verifiy_user(login_user_id,login_user_password):
    print("Login user "+ login_user_id)
    login_reponse = identity_management_client.token(token=b64_encoded_custos_token, username=login_user_id, password=login_user_password, grant_type='password')
    login_reponse = MessageToJson(login_reponse)
    print("Login response: ", login_reponse)
    response = user_management_client.get_user(token=b64_encoded_custos_token, username=login_user_id)
    print(" Updating user profile...  ")
    user_management_client.update_user_profile(
        token=b64_encoded_custos_token,
        username=response.username,
        email=response.email,
        first_name=response.first_name,
        last_name=response.last_name)
    print(" User  "+ login_user_id + " successfully logged in and updated profile")

print("verifiy_user method is defined")

try:
  verifiy_user(admin_user_name,admin_password)
  print("Successfully verified user")
except Exception as e:
  print("verifiy_user is not defined or user may not be created  in the teanant"+ str(e))
```

::: 
    Successfully configured all custos clients
    verifiy_user method is defined
    Login user aarvind@iu.edu
:::


## Register new users
``` {.python}
def register_users(users):
    for user in users:
        print("Registering user: " + user['username'])
        try:
          user_management_client.register_user(token=b64_encoded_custos_token,
                                             username=user['username'],
                                             first_name=user['first_name'],
                                             last_name=user['last_name'],
                                             password=user['password'],
                                             email=user['email'],
                                             is_temp_password=False)
          user_management_client.enable_user(token=b64_encoded_custos_token, username=user['username'])
        except Exception:
          print("User may be already exist")
print("register_users method is defined")
```

::: {.output .stream .stdout}
    register_users method is defined
:::
:::

::: {.cell .code execution_count="4" colab="{\"base_uri\":\"https://localhost:8080/\"}" id="876kZpuTE4hM" outputId="42a2d1f6-108e-42f3-a38e-77fd3dc5cc6c"}
``` {.python}
users = [
    {
        'username': 'alice122',
        'first_name': 'Alice',
        'last_name': 'Aron',
        'password': '12345678',
        'email': 'alice@gmail.com'
    },
    {
        'username': 'audrey122',
        'first_name': 'Audrey',
        'last_name': 'Aron',
        'password': '12345678',
        'email': 'audrey@gmail.com'
    },
    {
        'username': 'sophia122',
        'first_name': 'Sophia',
        'last_name': 'Aron',
        'password': '12345678',
        'email': 'sophia@gmail.com'
    },
    {
        'username': 'abelota122',
        'first_name': 'Abelota',
        'last_name': 'Aron',
        'password': '12345678',
        'email': 'abelota@gmail.com'
    },
    {
        'username': 'abigaill122',
        'first_name': 'Abigaill',
        'last_name': 'Aron',
        'password': '12345678',
        'email': 'abigaill@gmail.com'
    },
    {
        'username': 'adalee122',
        'first_name': 'Adalee',
        'last_name': 'Aron',
        'password': '12345678',
        'email': 'adalee@gmail.com'
    }
]

try:  
 register_users(users)
except Exception:
 print("please defined method register_users")
```

::: {.output .stream .stdout}
    Registering user: alice122
    Registering user: audrey122
    Registering user: sophia122
    Registering user: abelota122
    Registering user: abigaill122
    Registering user: adalee122
:::
:::

## Create groups

-   Admin group
-   Read only admin group
-   Gateway users group

``` {.python}
def create_groups(groups):
    for group in groups:
      try:
        print("Creating group: " + group['name'])
        grResponse = group_management_client.create_group(token=b64_encoded_custos_token,
                                                           name=group['name'],
                                                           description=group['description'],
                                                           owner_id=group['owner_id'])
        resp = MessageToJson(grResponse)
        print(resp)
        respData = json.loads(resp)
        print("Created group id of "+ group['name'] + ": " +respData['id'] )
        created_groups[respData['name']] = respData['id']
      except Exception as e:
        print(e)
        print("Group may be already created")
print("create_groups method is defined")
```

::: {.output .stream .stdout}
    create_groups method is defined
:::
:::

::: {.cell .code execution_count="6" colab="{\"base_uri\":\"https://localhost:8080/\"}" id="tGOqPs2cINQ9" outputId="ec58e3cc-bd15-45bf-fc82-181adc71a8c5"}
``` {.python}
groups = [
    {
        'name': 'Admin',
        'description': 'Group for gateway read only admins',
        'owner_id': admin_user_name
    },
    {
        'name': 'Read Only Admin',
        'description': 'Group for gateway admins',
        'owner_id': admin_user_name
    },
    {
        'name': 'Gateway User',
        'description': 'Group  for gateway users',
        'owner_id': admin_user_name
    }
]
try :
  create_groups(groups)
except Exception as e:
  print(e)
  print("please defined method create_groups")
```

    Creating group: Admin
    {
      "id": "admin_8b21ce4b-fd8d-4a93-a4a5-9851af8f1cc5",
      "name": "Admin",
      "createdTime": "1651759347000",
      "lastModifiedTime": "1651759347000",
      "description": "Group for gateway read only admins",
      "ownerId": "aarvind@iu.edu"
    }
    Created group id of Admin: admin_8b21ce4b-fd8d-4a93-a4a5-9851af8f1cc5
    Creating group: Read Only Admin
    {
      "id": "read_only_admin_1062fd1d-e63b-4b99-8f13-1dddb06581d5",
      "name": "Read Only Admin",
      "createdTime": "1651759347000",
      "lastModifiedTime": "1651759347000",
      "description": "Group for gateway admins",
      "ownerId": "aarvind@iu.edu"
    }
    Created group id of Read Only Admin: read_only_admin_1062fd1d-e63b-4b99-8f13-1dddb06581d5
    Creating group: Gateway User
    {
      "id": "gateway_user_fe43e65b-565a-4be9-9f54-d8d099b31a7a",
      "name": "Gateway User",
      "createdTime": "1651759347000",
      "lastModifiedTime": "1651759347000",
      "description": "Group  for gateway users",
      "ownerId": "aarvind@iu.edu"
    }
    Created group id of Gateway User: gateway_user_fe43e65b-565a-4be9-9f54-d8d099b31a7a

## Alocate users to groups

-   Admin : alice, audery
-   Read only admin : sophia,abelota
-   Gateway User : abgaill, adalee

``` {.python}
def allocate_users_to_groups(user_group_mapping):
    for usr_map in user_group_mapping:
      try:
        group_id = created_groups[usr_map['group_name']]
        print("Assigning user " + usr_map['username'] + " to group " + usr_map['group_name'])
        val =group_management_client.add_user_to_group(token=b64_encoded_custos_token,
                                                  username=usr_map['username'],
                                                  group_id=group_id,
                                                  membership_type='Member'
                                                  )
        resp = MessageToJson(val)
        print(resp)
      except Exception as e:
        print(e)
        print("User allocation error")
print("allocate_users_to_groups method is defined")
```

    allocate_users_to_groups method is defined

``` {.python}
user_group_mapping = [
    {
        'group_name': 'Admin',
        'username': 'alice'
    },
    {
        'group_name': 'Admin',
        'username': 'audrey122'
    },
    {
        'group_name': 'Read Only Admin',
        'username': 'sophia122'
    },
    {
        'group_name': 'Read Only Admin',
        'username': 'abelota122'
    },
    {
        'group_name': 'Gateway User',
        'username': 'abigaill122'
    },
    {
        'group_name': 'Gateway User',
        'username': 'adalee122'
    }
]

try:
  allocate_users_to_groups(user_group_mapping)
except Exception:
  print("please defined method allocate_users_to_groups")
```

::: {.output .stream .stdout}
    Assigning user alice to group Admin
    {
      "status": true
    }
    Assigning user audrey122 to group Admin
    {
      "status": true
    }
    Assigning user sophia122 to group Read Only Admin
    {
      "status": true
    }
    Assigning user abelota122 to group Read Only Admin
    {
      "status": true
    }
    Assigning user abigaill122 to group Gateway User
    {
      "status": true
    }
    Assigning user adalee122 to group Gateway User
    {
      "status": true
    }
:::
:::

::: {.cell .markdown id="wJPSkec3MwoF"}
## Create group hierarchy

-   Assign Admin group as a child of Read Only Admin group
:::

``` {.python}
def allocate_child_group_to_parent_group(gr_gr_mapping):
    for gr_map in gr_gr_mapping:
      try:
        child_id = created_groups[gr_map['child_name']]
        parent_id = created_groups[gr_map['parent_name']]
        print("Assigning child group " + gr_map['child_name'] + " to parent group " + gr_map['parent_name'])
        group_management_client.add_child_group(token=b64_encoded_custos_token,
                                                parent_group_id=parent_id,
                                                child_group_id=child_id)
      except Exception:
        print("Child group allocation error")
print("allocate_child_group_to_parent_group method is defined")
```

:::
    allocate_child_group_to_parent_group method is defined


``` {.python}
child_gr_parent_gr_mapping = [
    {
        "child_name": 'Admin',
        "parent_name": 'Read Only Admin'
    }
]

try:
  allocate_child_group_to_parent_group(child_gr_parent_gr_mapping)
except Exception:
  print("please defined method allocate_child_group_to_parent_group")
```


## Create Permissions

-   WRITE
-   READ
:::


``` {.python}
def create_permissions(permissions):
    for perm in permissions:
        print("Creating permission " + perm['id'])
        try:
         sharing_management_client.create_permission_type(token=b64_encoded_custos_token,
                                                         client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                         id=perm['id'],
                                                         name=perm['name'],
                                                         description=perm['description'])
        except Exception:
           print("Permission may be already created")
print("create_permissions method is defined")
```

    create_permissions method is defined

``` {.python}
permissions = [
    {
        'id': 'READ',
        'name': 'READ',
        'description': 'Read permission'
    },
    {
        'id': 'WRITE',
        'name': 'WRITE',
        'description': 'WRITE permission'
    }
]
try :
  create_permissions(permissions)
except Exception:
  print("please defined method create_permissions")
```

::: {.output .stream .stdout}
    Creating permission READ
    Creating permission WRITE
:::
:::

::: {.cell .markdown id="2phR6Ew4QA3N"}
## Create entity type

Categorization of entities (digital objects) you want to share

-   Project
-   Experiment

``` {.python}
def create_entity_types(entity_types):
    for type in entity_types:
        print("Creating entity types " + type['id'])
        try:
          sharing_management_client.create_entity_type(token=b64_encoded_custos_token,
                                                     client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                     id=type['id'],
                                                     name=type['name'],
                                                     description=type['description'])
        except Exception:
          print("Entity type may be already created")
print("create_entity_types method is defined")
```


    create_entity_types method is defined


``` {.python}
entity_types = [
    {
        'id': 'PROJECT',
        'name': 'PROJECT',
        'description': 'PROJECT entity type'
    },
    {
        'id': 'EXPERIMENT',
        'name': 'EXPERIMENT',
        'description': 'EXPERIMENT entity type'
    }
]
try :
  create_entity_types(entity_types)
except Exception:
  print("please defined method create_entity_types")
```

::: {.output .stream .stdout}
    Creating entity types PROJECT
    Creating entity types EXPERIMENT
:::
:::

::: {.cell .markdown id="9zXM0TcGRHR6"}
## Register entity

-   create an experiment

``` {.python}
def register_resources(resources):
    for resource in resources:
        id =  resource['name'].join(random.choice(string.ascii_letters) for x in range(5))
        resource_ids.append(id)
        resource['id']=id
        print("Register resources " + resource['name'] + " generated ID : "+resource['id'] )
        sharing_management_client.create_entity(token=b64_encoded_custos_token,
                                                client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                id=resource['id'],
                                                name=resource['name'],
                                                description=resource['description'],
                                                owner_id=resource['user_id'],
                                                type=resource['type'],
                                                parent_id='')
print("register_resources method is defined")
```
    register_resources method is defined
``` {.python}
resources = [
    {
        'name': 'SEAGRD_EXP',
        'description': 'Register an experiment',
        'user_id': admin_user_name,
        'type': 'EXPERIMENT'
    }
]
try:   
  register_resources(resources)
except Exception as e:
  print("Please defined method register_resources")
```
    Register resources SEAGRD_EXP generated ID : vSEAGRD_EXPcSEAGRD_EXPlSEAGRD_EXPsSEAGRD_EXPt
:::
:::
## Share resource with group

-   Share experiment under READ permissions with group Read Only Admin

``` {.python}
def share_resource_with_group(gr_sharings):
    for shr in gr_sharings:
      try:
        group_id = created_groups[shr['group_name']]
        print("Sharing entity " + shr['entity_id'] + " with group " + shr['group_name'] + " with permission " + shr[
            'permission_type'])
        sharing_management_client.share_entity_with_groups(token=b64_encoded_custos_token,
                                                           client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                           entity_id=shr['entity_id'],
                                                           permission_type=shr['permission_type'],
                                                           group_id=group_id)
      except Exception as e:
        print("Sharing may be already created: "+ str(e))
print("share_resource_with_group method is defined")
```


    share_resource_with_group method is defined

``` {.python}
gr_sharings = [{

    "entity_id": resource_ids[0],
    "permission_type": "READ",
    "type": "EXPERIMENT",
    "group_name": 'Read Only Admin'
}]
try:
  share_resource_with_group(gr_sharings)
except Exception as e:
  print("please defined method share_resource_with_group")
```

::: {.output .stream .stdout}
    Sharing entity vSEAGRD_EXPcSEAGRD_EXPlSEAGRD_EXPsSEAGRD_EXPt with group Read Only Admin with permission READ
:::
:::

::: {.cell .markdown id="RAWb7-1DXNnB"}
## Share entity with a user

-   Share registered experiment with a user in gateway user group

``` {.python}
def share_resource_with_user(sharings):
    for shr in sharings:
      try:
        print("Sharing entity " + shr['entity_id'] + " with user " + shr['user_id'] + " with permission " + shr[
            'permission_type'])
        sharing_management_client.share_entity_with_users(token=b64_encoded_custos_token,
                                                          client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                          entity_id=shr['entity_id'],
                                                          permission_type=shr['permission_type'],
                                                          user_id=shr['user_id']
                                                          )
      except Exception as e:
        print("Sharing error :"+str(e))
print("share_resource_with_user method is defined")
```
    share_resource_with_user method is defined
``` {.python}
sharings = [
    {
        "entity_id": resource_ids[0],
        "permission_type": "READ",
        "type": "EXPERIMENT",
        "user_id": "abigaill122"
    }
]
try :  
   share_resource_with_user(sharings)
except Exception as e:
   print("Please defined method share_resource_with_user")
```
    Sharing entity vSEAGRD_EXPcSEAGRD_EXPlSEAGRD_EXPsSEAGRD_EXPt with user abigaill122 with permission READ

## Evaluate permissions

### Expected result

-   alice : True\
-   audrey : True
-   sophia: True *abelota: True *abigaill: True \*adalee: False

``` {.python}
def check_user_permissions(users):
    for user in users:
      try:
        access = sharing_management_client.user_has_access(token=b64_encoded_custos_token,
                                                           client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                           entity_id=resource_ids[0],
                                                           permission_type="READ",
                                                           user_id=user['username'])
        usr = user['username']
        print("Access for user " + usr + " : " + str(access))
      except Exception as e:
        print("Permission checking error "+ str(e))
print("check_user_permissions is defined")
```

    check_user_permissions is defined
``` {.python}
try: 
 check_user_permissions(users)
except Exception as e:
 print(e)
 print("please defined methos check_user_permissions")
```

::: {.output .stream .stdout}
    Access for user alice122 : False
    Access for user audrey122 : True
    Access for user sophia122 : True
    Access for user abelota122 : True
    Access for user abigaill122 : True
    Access for user adalee122 : False
# Secret Management

## Create SSH Key

``` {.python}
def create_SSH_key(user_id,description):
 response = resource_secret_client.add_ssh_credential(token=b64_encoded_custos_token,client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                      owner_id=user_id,description=description)
 return response.token
  
```

## Fetch SSH Key

``` {.python}
def get_SSH_key(token):
 return resource_secret_client.get_ssh_credential(token=b64_encoded_custos_token,client_id=custos_settings.CUSTOS_CLIENT_ID,ssh_credential_token=token)
```

## Run create SSH Key and Fetch SSH key

``` {.python}
token = create_SSH_key(admin_user_name,'SSH for gateway')
sharing_management_client.create_entity(token=b64_encoded_custos_token,
                                                client_id=custos_settings.CUSTOS_CLIENT_ID,
                                                id=token,
                                                name='SSH Key',
                                                description='SSH for gateway',
                                                owner_id=admin_user_name,
                                                type='SECRET',
                                                parent_id='')
value = get_SSH_key(token)
print(value)
```

    {
      "metadata": {
        "resourceType": "VAULT_CREDENTIAL",
        "source": "EXTERNAL",
        "tenantId": "10003418",
        "ownerId": "aarvind@iu.edu",
        "persistedTime": "1651759584000",
        "token": "397c1811-4920-402f-864c-f3468a1f5697",
        "description": "SSH for gateway"
      },
      "passphrase": "0f28f85d-d5fb-4375-825c-9b002c66acb9",
      "publicKey": "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCMTPaQEDMQCrNZVmhZ4WbH7+q621iAYBK6qp6CyN2H0bugI+dKGIDKB4q26qlnn4TBiOdVZ4C/znbp4CSLUMOXG5DaMuEODl8axJXDKOW8KyWAXPhzy775RYx0RZXfg/WM0ILkFy626SDDUXiV8uW+eGK4L29YVRdTq5eJipKB3DBp1Mj8VnGYYCJ6iWiBLeRJ+sRVYPt0wEI6PueBiH/ozg/yraFWNA8YRg8CEYctZgTzv/luf8cEsR7/Zt6KIgxorPDV1ha+ri2sSdBarhDcOQDTr6qmLJUYKR3geQqjQUFlYzCWFoZSmC9fpBiADbAJoIbchk6h6WzY0Fu4A/fV \n",
      "privateKey": "-----BEGIN RSA PRIVATE KEY-----\nProc-Type: 4,ENCRYPTED\nDEK-Info: DES-EDE3-CBC,1EA731EBDCD7DDD1\n\nYdM0RzRacq3cpTlRD0ii8YsQPcLEqeqmLDu5uw9thMrYUreQqWKhS9NIjIqt1wFs\n/j7us2PUzIekAi/6lf/d9/HlEg+p9Ycuh2slUf9IFUbicLfFIgjcTP05WVg8YANN\nuzmkwgETuDax7xwzgZkgiLbjXfGq+Y8GeVmdeHMNSo28A0hhi/54pWBR0VxNYK9U\nZq03Z6DjgRRf6XpocFGsaTNRCJ0NJ4PCb9io1jfCepqrxfgJrMlLe0bfCOkSWt8z\npm3zn5CUFHrS/59w2ocmxPEAfZB2Pi9dVkFBdGskBFbnOb7rstq3JSPEIsbqvp9o\n6h6oWqfmSn4EDr8SEWTaYG4WEa9O61RPYugkSA48sw8hEpw5p8iCbA2q6DCiyneM\nM08CAvQuXGkVjFbP84qpL3CbnLR332aaQEWj8SlYNv3a2TMfOfc9Wm+x7vFJkWLl\nmeQsgT+D/hQR/Dd9iuKe4HyKHIpvKbOjHZwViLYHmBwmHvVo5FnD54P1lzOoSjfb\nR7q320eXP/LJK1nITIpX/VHk1Raq+P+7J3tUv9vaIZ5cIUMmkk2OUM+98DKt9UGy\nwu+BF/GSnkZ4QQYaeSX6fDteR7om/IWmgKDsSlbs+yzVAJsFAUJCdQSvJXreSrwv\nqKdFG4vBfCUGRoXHVDWiHIQ3S3qdp89cXaeWDAK2+ZuQu6AyoRS/5zNU5b51QIu8\n3zxNGtddwzI/VJtUAnHE5+8qwHEMURoZtihGzeVcitrhWC4iWvBCbcAQCCVXl/YC\nlF/lseWDI9YA/Rb1iSAcIICS03kq7uTeHURkDJonzPTIdSbhW2nDtmgaN1yxw8a5\nedfqQhaOJE2BwuHIuDPwP6Fpgxu70iolaexSxSVCgMgOH1ISyb1qQTUtDSztCqy9\nxixWtRcTuYbhhhAR22V/3J34LPzhhkGkxgSlzDSy0OjYervnm5mgUKpGiDvZvaZe\ni+G2Us7gpWh/avBRo1bIdDAjbLl6W/sIYkbWf5JQwCdD6VNJ8zAW6PUN+byPcISn\noSJERBrkK3nBoRqQLa3KPQI9LGIe3LCtqN2QsYMzDPbZTWKBoyVVxFYKBR+jzmMF\nvFeBc3JPOOtQXOqw+aZ0fZd3+gFZq11dWQ+Vbc85XaPcbE73JSXKwG6VKGDL/SNF\nXgiU0ViiyjRaXninCwIRL1HhPmWtsA+n60ucfdspI430W64eHd6z6DTaJlIRycSe\nfeeuW/n0Szu5J7XZRDU13ZDiSB0HXZspACwqeKTNCSyOesWo/19M0WWZkxZ5OYI1\n5jlAc7YhnpgTd2NJhVLYNazmp9cH8vnKtnbNrF9UbCpMc8Wjgfe5LyxthipIXITY\nnjg9BG5fqgDN1N7DPWvYgHa9OxpFlHDesGTRb/sXK41pbu61GadXWPHzIm8+Gf8S\npM4VxR2prupgGrDuGD2qFxoEt6ellPEblrGdPBfyTd34wVXThTszEZxG+xu3YQty\nkTe4rsxFMRRdps+drd6xOJiBhRayKTSxTkMQBXu0/jcMGu8tooR8Z8kag2tyzeEM\nI+G7yk21nfSzDe0hSgOOAtJa3f2tdbzIRU/45z1GFZmC/KqJCcqMMPU4UJ6OWglX\n-----END RSA PRIVATE KEY-----\n"
    }


``` {.python}
```
