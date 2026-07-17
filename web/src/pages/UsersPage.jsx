import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'username', header: 'Username' },
  { key: 'name', header: 'Name' },
  { key: 'role', header: 'Role' },
  { key: 'badge', header: 'Badge' },
  { key: 'status', header: 'Status' },
  { key: 'vehicleCode', header: 'Vehicle' },
];

export default function UsersPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchUsers(), []);

  return (
    <section>
      <h2>Users</h2>
      {isLoading && <p>Loading…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="No users" />}
    </section>
  );
}
