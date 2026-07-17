import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'code', header: 'Code' },
  { key: 'status', header: 'Status' },
  { key: 'priority', header: 'Priority' },
  { key: 'type', header: 'Type' },
  { key: 'ambulanceCode', header: 'Ambulance' },
  { key: 'driver', header: 'Driver' },
  { key: 'etaMinutes', header: 'ETA (min)' },
];

export default function MissionsPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchMissions(), []);

  return (
    <section>
      <h2>Missions</h2>
      {isLoading && <p>Loading…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="No missions" />}
    </section>
  );
}
