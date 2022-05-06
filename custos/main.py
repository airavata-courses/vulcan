from fastapi import FastAPI, Request
import uvicorn
from custosUtility.identityManagement import IdentityManagement
from custosUtility.userManagement import UserManagement
from custosUtility.shareManagement import ShareManagement
from custosUtility.secretManagement import SecretManagement
from custosUtility.groupManagement import GroupManagement


app = FastAPI()

@app.post('/register')
async def register(userRequest:Request):
    userRequest = await userRequest.json()
    return UserManagement().registerUser(userRequest)

@app.post('/create_group')
async def create_group(groupRequest:Request):
    groupRequest = await groupRequest.json()
    return GroupManagement().create_group(groupRequest)

# @app.get('/getUser')
# async def getUser(getUserRequest:Request):
#     getUserRequest = await getUserRequest.json()
#     return UserManagement().getUser(getUserRequest)

# @app.post('/login')
# async def login(loginRequest:Request):
#     loginRequest = await loginRequest.json()
#     return IdentityManagement().login(loginRequest)

# @app.post('/logout')
# async def login(logoutRequest:Request):
#     logoutRequest = await logoutRequest.json()
#     return IdentityManagement().logout(logoutRequest)

if __name__ == '__main__':
    uvicorn.run(app, host='0.0.0.0', port=3000)


