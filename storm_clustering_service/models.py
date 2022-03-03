from pydantic import BaseModel
from typing import List


class Geometry(BaseModel):
    type: str
    coordinates: List[List[List[float]]]


class Feature(BaseModel):
    type: str
    property: object
    geometry: Geometry


class ForecastGeoJson(BaseModel):
    type: str
    features: List[Feature]
