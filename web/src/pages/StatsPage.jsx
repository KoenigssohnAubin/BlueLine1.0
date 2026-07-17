import * as api from '../api';
import { useFetch } from '../hooks/useFetch';

const PERIODS = [
  { key: 'today', label: 'Today' },
  { key: 'week', label: 'This week' },
  { key: 'month', label: 'This month' },
];

export default function StatsPage() {
  const { data, error, isLoading } = useFetch(
    () => Promise.all(PERIODS.map((p) => api.fetchStats(p.key))),
    []
  );

  return (
    <section>
      <h2>Stats</h2>
      {isLoading && <p>Loading…</p>}
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
                  <dt>Completed</dt>
                  <dd>{s.completed}</dd>
                  <dt>Avg. duration</dt>
                  <dd>{s.avgTime}</dd>
                  <dt>Success rate</dt>
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
