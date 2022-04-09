from datetime import datetime
from siphon.radarserver import RadarServer, RadarQuery, TDSCatalog
from typing import Optional


class RadarService:
    def __init__(self):
        self.server = RadarServer(
            'http://tds-nexrad.scigw.unidata.ucar.edu/thredds/radarServer/nexrad/level2/S3/')

    def get_query(self, start_time: datetime, end_time: datetime, longitude: float, latitude: float) -> RadarQuery:
        query = self.server.query()
        query.lonlat_point(lon=longitude, lat=latitude).time_range(
            start_time, end_time)
        if not self.server.validate_query(query):
            raise ValueError('One or more provided parameter(s) are invalid.')
        return query

    def get_catalog(self, query: RadarQuery) -> Optional[TDSCatalog]:
        if not self.server.validate_query(query):
            return
        catalog = self.server.get_catalog(query)
        return catalog
