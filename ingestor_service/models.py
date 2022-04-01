from pydantic import BaseModel
from typing import List


class ClientRequest(BaseModel):
    year: str
    month: str
    date: str
    coords: List[float]
