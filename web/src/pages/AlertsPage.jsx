import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'type', header: 'Type' },
  { key: 'severity', header: 'Severity' },
  { key: 'message', header: 'Message' },
  { key: 'location', header: 'Location' },
  { key: 'active', header: 'Active', render: (row) => (row.active ? 'Yes' : 'No') },
  { key: 'reportedBy', header: 'Reported by' },
];

export default function AlertsPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchAlerts(), []);

  return (
    <section>
      <h2>Alerts</h2>
      {isLoading && <p>Loading…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="No alerts" />}
    </section>
  );
}
