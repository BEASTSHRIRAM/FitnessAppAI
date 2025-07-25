import React, { useState, useEffect } from 'react';
import { useUser } from './UserContext';

const LIFT_TYPES = [
  'Squat',
  'Bench Press',
  'Deadlift',
  'Bent-over Row',
  'Lat Pulldown',
];

interface PRRecord {
  id?: string;
  lift: string;
  date: string;
  weight: string;
}

const PersonalRecords: React.FC = () => {
  const { user } = useUser();
  const userId = user?.id;
  const [records, setRecords] = useState<PRRecord[]>([]);
  const [form, setForm] = useState<PRRecord>({ lift: LIFT_TYPES[0], date: '', weight: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!userId) return;
    setLoading(true);
    // TODO: Implement backend endpoint /api/fitness/pr/{userId}
    fetch(`http://localhost:9090/api/fitness/pr/${userId}`)
      .then(res => res.json())
      .then(data => {
        setRecords(data);
        setLoading(false);
      })
      .catch(() => {
        setError('Could not fetch personal records');
        setLoading(false);
      });
  }, [userId]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.lift || !form.date || !form.weight) return;
    if (!userId) {
      setRecords([{ ...form }, ...records]);
      setForm({ lift: LIFT_TYPES[0], date: '', weight: '' });
      return;
    }
    setLoading(true);
    setError('');
    try {
      // TODO: Implement backend endpoint /api/fitness/pr/{userId}
      const res = await fetch(`http://localhost:9090/api/fitness/pr/${userId}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(form)
        });
      if (!res.ok) throw new Error('Failed to save');
      const saved = await res.json();
      setRecords([saved, ...records]);
      setForm({ lift: LIFT_TYPES[0], date: '', weight: '' });
    } catch {
      setError('Could not save record');
    }
    setLoading(false);
  };

  // Show only the latest record for each lift
  const latestRecords = LIFT_TYPES.map(lift =>
    records.find(record => record.lift === lift)
  ).filter(Boolean) as PRRecord[];

  return (
    <div className="bg-white rounded-xl shadow p-6 w-full max-w-2xl mx-auto">
      <h3 className="text-xl font-bold mb-4">Personal Records (PR)</h3>
      <form className="flex flex-col gap-3 mb-6" onSubmit={handleSubmit}>
        <div className="flex gap-2">
          <select name="lift" value={form.lift} onChange={handleChange} className="border p-2 rounded flex-1">
            {LIFT_TYPES.map(lift => (
              <option key={lift} value={lift}>{lift}</option>
            ))}
          </select>
          <input type="date" name="date" value={form.date} onChange={handleChange} className="border p-2 rounded flex-1" required />
          <input type="number" name="weight" value={form.weight} onChange={handleChange} placeholder="Weight (kg)" className="border p-2 rounded flex-1" required />
        </div>
        <button type="submit" className="bg-gradient-to-r from-pink-500 to-yellow-500 text-white py-2 rounded shadow hover:scale-105 transition-transform mt-2" disabled={loading}>{loading ? 'Saving...' : 'Add Record'}</button>
      </form>
      {error && <div className="text-red-500 mb-2">{error}</div>}
      <h4 className="text-lg font-semibold mb-2">Latest PRs</h4>
      {loading && records.length === 0 ? (
        <div className="text-gray-400">Loading...</div>
      ) : latestRecords.length === 0 ? (
        <div className="text-gray-400">No personal records yet.</div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          {latestRecords.map((record, idx) => (
            <div key={record.id || idx} className="bg-yellow-50 rounded-lg p-4 shadow-sm border border-yellow-100 flex flex-col items-center">
              <div className="text-lg font-bold text-pink-700 mb-1">{record.lift}</div>
              <div className="text-2xl font-extrabold text-yellow-600">{record.weight} kg</div>
              <div className="text-xs text-gray-500 mt-1">{record.date}</div>
            </div>
          ))}
        </div>
      )}
      <h4 className="text-lg font-semibold mt-6 mb-2">All PR History</h4>
      {records.length === 0 ? (
        <div className="text-gray-300">No history yet.</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full text-sm text-left border rounded">
            <thead className="bg-yellow-50">
              <tr>
                <th className="py-2 px-3">Lift</th>
                <th className="py-2 px-3">Date</th>
                <th className="py-2 px-3">Weight (kg)</th>
              </tr>
            </thead>
            <tbody>
              {records.map((record, idx) => (
                <tr key={record.id || idx} className="even:bg-gray-50">
                  <td className="py-1 px-3">{record.lift}</td>
                  <td className="py-1 px-3 font-mono">{record.date}</td>
                  <td className="py-1 px-3">{record.weight}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default PersonalRecords; 