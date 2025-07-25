import React, { useState, useEffect } from 'react';
import { useUser } from './UserContext';

interface WorkoutEntry {
  id?: string;
  date: string;
  workout: string;
  notes: string;
}

const MyWorkouts: React.FC = () => {
  const { user } = useUser();
  const userId = user?.id;
  const [entries, setEntries] = useState<WorkoutEntry[]>([]);
  const [form, setForm] = useState<WorkoutEntry>({ date: '', workout: '', notes: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!userId) return;
    setLoading(true);
    fetch(`http://localhost:9090/api/fitness/workout-log/${userId}`)
      .then(res => res.json())
      .then(data => {
        setEntries(data);
        setLoading(false);
      })
      .catch(() => {
        setError('Could not fetch workout logs');
        setLoading(false);
      });
  }, [userId]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.date || !form.workout) return;
    if (!userId) {
      setEntries([{ ...form }, ...entries]);
      setForm({ date: '', workout: '', notes: '' });
      return;
    }
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`http://localhost:9090/api/fitness/workout-log/${userId}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            date: form.date,
            workout: form.workout,
            notes: form.notes,
          })
        });
      if (!res.ok) throw new Error('Failed to save');
      const saved = await res.json();
      setEntries([saved, ...entries]);
      setForm({ date: '', workout: '', notes: '' });
    } catch {
      setError('Could not save workout');
    }
    setLoading(false);
  };

  return (
    <div className="bg-white rounded-xl shadow p-6 w-full max-w-2xl mx-auto">
      <h3 className="text-xl font-bold mb-4">Log Today's Workout</h3>
      <form className="flex flex-col gap-3 mb-6" onSubmit={handleSubmit}>
        <div className="flex gap-2">
          <input type="date" name="date" value={form.date} onChange={handleChange} className="border p-2 rounded flex-1" required />
        </div>
        <textarea name="workout" value={form.workout} onChange={handleChange} placeholder="Workout details (e.g., exercises, sets, reps, cardio, etc.)" className="border p-2 rounded resize-none" rows={3} required />
        <textarea name="notes" value={form.notes} onChange={handleChange} placeholder="Notes, injuries, or anything else (optional)" className="border p-2 rounded resize-none" rows={2} />
        <button type="submit" className="bg-gradient-to-r from-blue-500 to-purple-500 text-white py-2 rounded shadow hover:scale-105 transition-transform mt-2" disabled={loading}>{loading ? 'Saving...' : 'Add Workout'}</button>
      </form>
      {error && <div className="text-red-500 mb-2">{error}</div>}
      <h4 className="text-lg font-semibold mb-2">Workout History</h4>
      {loading && entries.length === 0 ? (
        <div className="text-gray-400">Loading...</div>
      ) : entries.length === 0 ? (
        <div className="text-gray-400">No workouts logged yet.</div>
      ) : (
        <ul className="space-y-4">
          {entries.map((entry, idx) => (
            <li key={entry.id || idx} className="bg-blue-50 rounded-lg p-4 shadow-sm border border-blue-100">
              <div className="flex items-center justify-between mb-2">
                <span className="font-mono text-sm text-blue-700">{entry.date}</span>
                {entry.notes && <span className="text-xs text-red-500 font-semibold">⚠️ {entry.notes}</span>}
              </div>
              <div className="text-gray-800 whitespace-pre-line">{entry.workout}</div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default MyWorkouts; 