import React, { useState, useEffect } from 'react';
import { useUser } from './UserContext';

interface ProgressEntry {
  id?: string;
  date: string;
  weight: string;
  waist: string;
  notes: string;
}

const ProgressEntries: React.FC = () => {
  const { user } = useUser();
  const userId = user?.id;
  const [entries, setEntries] = useState<ProgressEntry[]>([]);
  const [form, setForm] = useState<ProgressEntry>({ date: '', weight: '', waist: '', notes: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!userId) return;
    setLoading(true);
    fetch(`http://localhost:9090/api/fitness/progress/${userId}`)
      .then(res => res.json())
      .then(data => {
        setEntries(data);
        setLoading(false);
      })
      .catch(() => {
        setError('Could not fetch progress entries');
        setLoading(false);
      });
  }, [userId]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.date || !form.weight) return;
    if (!userId) {
      setEntries([{ ...form }, ...entries]);
      setForm({ date: '', weight: '', waist: '', notes: '' });
      return;
    }
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`http://localhost:9090/api/fitness/progress/${userId}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            date: form.date,
            weight: parseFloat(form.weight),
            notes: form.notes,
            // Optionally add waist, bmi, etc.
          })
        });
      if (!res.ok) throw new Error('Failed to save');
      const saved = await res.json();
      setEntries([saved, ...entries]);
      setForm({ date: '', weight: '', waist: '', notes: '' });
    } catch {
      setError('Could not save entry');
    }
    setLoading(false);
  };

  return (
    <div className="bg-white rounded shadow p-6 w-full max-w-2xl mx-auto">
      <h3 className="text-xl font-bold mb-4">Log Progress</h3>
      <form className="flex flex-col gap-3 mb-6" onSubmit={handleSubmit}>
        <div className="flex gap-2">
          <input type="date" name="date" value={form.date} onChange={handleChange} className="border p-2 rounded flex-1" required />
          <input type="number" name="weight" value={form.weight} onChange={handleChange} placeholder="Weight (kg)" className="border p-2 rounded flex-1" required />
          <input type="number" name="waist" value={form.waist} onChange={handleChange} placeholder="Waist (cm)" className="border p-2 rounded flex-1" />
        </div>
        <textarea name="notes" value={form.notes} onChange={handleChange} placeholder="Notes (optional)" className="border p-2 rounded resize-none" rows={2} />
        <button type="submit" className="bg-blue-600 text-white py-2 rounded mt-2" disabled={loading}>{loading ? 'Saving...' : 'Add Entry'}</button>
      </form>
      {error && <div className="text-red-500 mb-2">{error}</div>}
      <h4 className="text-lg font-semibold mb-2">Progress History</h4>
      {loading && entries.length === 0 ? (
        <div className="text-gray-400">Loading...</div>
      ) : entries.length === 0 ? (
        <div className="text-gray-400">No entries yet.</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full text-sm text-left border rounded">
            <thead className="bg-blue-50">
              <tr>
                <th className="py-2 px-3">Date</th>
                <th className="py-2 px-3">Weight (kg)</th>
                <th className="py-2 px-3">Waist (cm)</th>
                <th className="py-2 px-3">Notes</th>
              </tr>
            </thead>
            <tbody>
              {entries.map((entry, idx) => (
                <tr key={entry.id || idx} className="even:bg-gray-50">
                  <td className="py-1 px-3 font-mono">{entry.date}</td>
                  <td className="py-1 px-3">{entry.weight}</td>
                  <td className="py-1 px-3">{entry.waist || '-'}</td>
                  <td className="py-1 px-3 text-gray-600">{entry.notes || '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default ProgressEntries; 