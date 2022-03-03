
from models import ClientRequest
import nexradaws


class NexRad:
    def __init__(self, data: ClientRequest) -> None:
        self.grpc_server = 'SERVER_STUB'
        self.grpc_server_port = 'PORT_STUB'
        self.date = data.date
        self.month = data.month
        self.year = data.year
        # self.station = data['station']
        # self.time = data['time']
        self.station = "KCCX"
        self.time = "12PM"

    # if there are radar scans available for the given date and station return true
    def validate(self):
        conn = nexradaws.NexradAwsInterface()
        try:
            scans = conn.get_avail_scans(
                self.year, self.month, self.date, self.station)
            return len(scans) != 0
        except:
            print('invalid user input')
            return False
