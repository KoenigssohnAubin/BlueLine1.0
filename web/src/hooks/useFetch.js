import { useEffect, useState, useCallback } from 'react';

export function useFetch(fetcher, deps = []) {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const reload = useCallback(() => {
    setIsLoading(true);
    setError(null);
    fetcher()
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setIsLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  useEffect(() => {
    reload();
  }, [reload]);

  return { data, error, isLoading, reload };
}
