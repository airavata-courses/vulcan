from datetime import datetime
from pydantic import BaseModel


class RadarRequest(BaseModel):
    start_time: datetime
    end_time: datetime
    longitude: float
    lattitude: float
