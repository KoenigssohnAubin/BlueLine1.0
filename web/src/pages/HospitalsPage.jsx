import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'name', header: 'Nom' },
  { key: 'address', header: 'Adresse' },
  { key: 'status', header: 'Statut' },
  { key: 'phone', header: 'Téléphone' },
  { key: 'score', header: 'Score' },
  {
    key: 'specialties',
    header: 'Spécialités',
    render: (row) => (row.specialties || []).join(', ') || '—',
  },
];

export default function HospitalsPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchHospitals(), []);

  return (
    <section>
      <h2>Hôpitaux</h2>
      {isLoading && <p>Chargement…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="Aucun hôpital" />}
    </section>
  );
}
