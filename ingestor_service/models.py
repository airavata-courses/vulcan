from ast import alias
from datetime import datetime
from pydantic import BaseModel


def to_camel(string: str) -> str:
    return ''.join(word.capitalize() if index else word for index, word in enumerate(string.split('_')))


class RadarRequest(BaseModel):
    start_time: datetime
    end_time: datetime
    longitude: float
    lattitude: float

    class Config:
        alias_generator = to_camel
