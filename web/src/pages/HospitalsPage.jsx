import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'name', header: 'Name' },
  { key: 'address', header: 'Address' },
  { key: 'status', header: 'Status' },
  { key: 'phone', header: 'Phone' },
  { key: 'score', header: 'Score' },
  {
    key: 'specialties',
    header: 'Specialties',
    render: (row) => (row.specialties || []).join(', ') || '—',
  },
];

export default function HospitalsPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchHospitals(), []);

  return (
    <section>
      <h2>Hospitals</h2>
      {isLoading && <p>Loading…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="No hospitals" />}
    </section>
  );
}
