from typing import Callable, List, Tuple
import cartopy.crs as ccrs
from concurrent.futures import ProcessPoolExecutor, wait
import io
import matplotlib.pyplot as plt
import metpy.plots as mpplots
import numpy as np
from siphon.radarserver import TDSCatalog


class GeoMapService:
    def __init__(self, longitude: float, latitude: float):
        self.longitude = longitude
        self.latitude = latitude
        # Set default projection to Mercator for accurate shapes on flat surface
        # Center around longitude of the weather station
        self.projection = ccrs.Mercator(central_longitude=longitude)
        # Initialize boundary norm and colormap for NEXRAD Reflectivity values
        self.ref_norm, self.ref_cmap = mpplots.ctables.registry.get_with_steps(
            name='NWSReflectivity', start=5, step=5)

    @staticmethod
    def plot_concurrent(catalog: TDSCatalog, plot_fn: Callable) -> List:
        executor = ProcessPoolExecutor(len(catalog.datasets))
        futures = [executor.submit(plot_fn, name, dataset)
                   for name, dataset in catalog.datasets.items()]
        settled = wait(futures)
        return [future.result() for future in settled.done]

    def plot_relectivity(self, name, dataset) -> Tuple[str, io.BytesIO]:
        data = dataset.remote_access()
        fig, geo_axes = self.__create_geo_axes()

        sweep = 0
        dist = data.variables['distanceR_HI'][:]
        azimuth = data.variables['azimuthR_HI'][sweep]
        ref_raw = data.variables['Reflectivity_HI']
        ref = self.__raw_to_masked_float(ref_raw, ref_raw[sweep])
        x, y = self.__polar_to_cartesian(azimuth, dist)

        geo_axes.pcolormesh(x, y, ref, cmap=self.ref_cmap,
                            norm=self.ref_norm, rasterized=True,
                            transform=ccrs.LambertConformal(
                                central_longitude=self.longitude, central_latitude=self.latitude))

        buffer = io.BytesIO()
        fig.savefig(buffer, format='png')
        buffer.seek(0)
        return (f'{name}.png', buffer)

    def __create_geo_axes(self, figsize=(31.54, 20)):
        fig = plt.figure(figsize=figsize)
        # Initialize GeoAxes instance
        xmin, ymin, dx, dy = 0.02, 0.02, 0.96, 0.96
        geo_axes = fig.add_axes(
            [xmin, ymin, dx, dy], projection=self.projection)

        fig.patch.set_alpha(0.)
        geo_axes.xaxis.set_visible(False)
        geo_axes.yaxis.set_visible(False)
        geo_axes.set_frame_on(False)

        return (fig, geo_axes)

    def __raw_to_masked_float(self, var, data):
        # Values come back signed. If the _Unsigned attribute is set, we need to convert
        # from the range [-127, 128] to [0, 255].
        if var._Unsigned:
            data = data & 255

        # Mask missing points
        data = np.ma.array(data, mask=data == 0)

        # Convert to float using the scale and offset
        return data * var.scale_factor + var.add_offset

    def __polar_to_cartesian(self, az, dist):
        az_rad = np.deg2rad(az)[:, None]
        x = dist * np.sin(az_rad)
        y = dist * np.cos(az_rad)
        return x, y
