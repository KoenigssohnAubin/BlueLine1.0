import * as api from '../api';
import { useFetch } from '../hooks/useFetch';

const PERIODS = [
  { key: 'today', label: "Aujourd'hui" },
  { key: 'week', label: 'Cette semaine' },
  { key: 'month', label: 'Ce mois' },
];

export default function StatsPage() {
  const { data, error, isLoading } = useFetch(
    () => Promise.all(PERIODS.map((p) => api.fetchStats(p.key))),
    []
  );

  return (
    <section>
      <h2>Statistiques</h2>
      {isLoading && <p>Chargement…</p>}
      {error && <p className="form-error">{error}</p>}
      {data && (
        <div className="stats-grid">
          {PERIODS.map((p, i) => {
            const s = data[i];
            return (
              <div className="stat-card" key={p.key}>
                <h3>{p.label}</h3>
                <dl>
                  <dt>Missions</dt>
                  <dd>{s.missions}</dd>
                  <dt>Terminées</dt>
                  <dd>{s.completed}</dd>
                  <dt>Durée moy.</dt>
                  <dd>{s.avgTime}</dd>
                  <dt>Taux de réussite</dt>
                  <dd>{s.successRate}%</dd>
                </dl>
              </div>
            );
          })}
        </div>
      )}
    </section>
  );
}
