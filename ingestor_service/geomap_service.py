from typing import List, Tuple
import cartopy.crs as ccrs
from concurrent.futures import ProcessPoolExecutor, wait
import io
import matplotlib.pyplot as plt
import metpy.plots as mpplots
import numpy as np
from siphon.radarserver import TDSCatalog


class GeoMapService:
    def __init__(self, longitude: float, latitude: float):
        # Create a projection centered on the weather station
        self.projection = ccrs.LambertConformal(
            central_longitude=longitude, central_latitude=latitude)

    def plot_reflectivity(self, catalog: TDSCatalog) -> List[Tuple[str, io.BytesIO]]:
        # Get colormap for NEXRAD Reflectivity values
        ref_norm, ref_cmap = mpplots.ctables.registry.get_with_steps(
            'NWSReflectivity', 5, 5)

        ds_names = [name for name in catalog.datasets]
        executor = ProcessPoolExecutor(len(ds_names))
        batch_fn = self.plot_relectivity_concurrent
        futures = [executor.submit(batch_fn, name, catalog.datasets[name],
                                   ref_norm, ref_cmap) for name in ds_names]
        settled = wait(futures)
        return [future.result() for future in settled.done]

    def plot_relectivity_concurrent(self, name, dataset, ref_norm, ref_cmap):
        data = dataset.remote_access()
        fig, geo_axes = self.__create_geo_axes()

        sweep = 0
        dist = data.variables['distanceR_HI'][:]
        azimuth = data.variables['azimuthR_HI'][sweep]
        ref_raw = data.variables['Reflectivity_HI']
        ref = self.__raw_to_masked_float(ref_raw, ref_raw[sweep])
        x, y = self.__polar_to_cartesian(azimuth, dist)

        geo_axes.pcolormesh(x, y, ref, cmap=ref_cmap,
                            norm=ref_norm, rasterized=True)

        buffer = io.BytesIO()
        fig.savefig(buffer, format='png')
        buffer.seek(0)
        return (f'{name}.png', buffer)

    def __create_geo_axes(self, figsize=(20, 20)):
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
