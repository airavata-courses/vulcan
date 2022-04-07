from pydantic import BaseModel
from typing import List


class MerraClientRequest(BaseModel):
    startdate: str
    enddate: str
    parameter: str