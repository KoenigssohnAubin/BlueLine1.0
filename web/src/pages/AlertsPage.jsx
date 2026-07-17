import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'type', header: 'Type' },
  { key: 'severity', header: 'Gravité' },
  { key: 'message', header: 'Message' },
  { key: 'location', header: 'Lieu' },
  { key: 'active', header: 'Actif', render: (row) => (row.active ? 'Oui' : 'Non') },
  { key: 'reportedBy', header: 'Signalé par' },
];

export default function AlertsPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchAlerts(), []);

  return (
    <section>
      <h2>Alertes</h2>
      {isLoading && <p>Chargement…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="Aucune alerte" />}
    </section>
  );
}
