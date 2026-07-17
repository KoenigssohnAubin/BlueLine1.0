import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'code', header: 'Code' },
  { key: 'model', header: 'Modèle' },
  { key: 'status', header: 'Statut' },
  { key: 'driver', header: 'Conducteur' },
  {
    key: 'position',
    header: 'Position',
    render: (row) => (row.lat && row.lng ? `${row.lat.toFixed(4)}, ${row.lng.toFixed(4)}` : '—'),
  },
];

export default function AmbulancesPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchAmbulances(), []);

  return (
    <section>
      <h2>Ambulances</h2>
      {isLoading && <p>Chargement…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="Aucune ambulance" />}
    </section>
  );
}
