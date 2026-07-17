import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import MapView from '../components/MapView';

export default function MapPage() {
  const { data: ambulances, error: ambError, isLoading: ambLoading } = useFetch(
    () => api.fetchAmbulances(),
    []
  );
  const { data: hospitals, error: hospError, isLoading: hospLoading } = useFetch(
    () => api.fetchHospitals(),
    []
  );
  const { data: missions, error: missError, isLoading: missLoading } = useFetch(
    () => api.fetchMissions('EN_COURS'),
    []
  );

  const isLoading = ambLoading || hospLoading || missLoading;
  const error = ambError || hospError || missError;

  return (
    <section>
      <h2>Live map</h2>
      {isLoading && <p>Loading…</p>}
      {error && <p className="form-error">{error}</p>}
      {!isLoading && !error && (
        <MapView ambulances={ambulances || []} hospitals={hospitals || []} missions={missions || []} />
      )}
    </section>
  );
}
