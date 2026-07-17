import * as api from '../api';
import { useFetch } from '../hooks/useFetch';
import DataTable from '../components/DataTable';

const COLUMNS = [
  { key: 'username', header: 'Identifiant' },
  { key: 'name', header: 'Nom' },
  { key: 'role', header: 'Rôle' },
  { key: 'badge', header: 'Badge' },
  { key: 'status', header: 'Statut' },
  { key: 'vehicleCode', header: 'Véhicule' },
];

export default function UsersPage() {
  const { data, error, isLoading } = useFetch(() => api.fetchUsers(), []);

  return (
    <section>
      <h2>Utilisateurs</h2>
      {isLoading && <p>Chargement…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && <DataTable columns={COLUMNS} rows={data} emptyLabel="Aucun utilisateur" />}
    </section>
  );
}
