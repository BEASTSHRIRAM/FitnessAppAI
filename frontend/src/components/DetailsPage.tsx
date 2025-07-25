import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { User } from './UserContext';

interface DetailsPageProps {
  user: User;
  onDetailsSubmit: (user: User) => void;
}

const DetailsPage: React.FC<DetailsPageProps> = ({ user, onDetailsSubmit }) => {
  const [age, setAge] = useState(user.age || '');
  const [height, setHeight] = useState(user.height || '');
  const [weight, setWeight] = useState(user.weight || '');
  const [activityLevel, setActivityLevel] = useState<number>(user.activityLevel || 3);
  const [fitnessGoal, setFitnessGoal] = useState(user.fitnessGoal || 'maintain');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!age || !height || !weight) {
      setMessage('Please fill in all fields.');
      return;
    }
    const updatedUser: User = {
      ...user,
      age: Number(age),
      height: Number(height),
      weight: Number(weight),
      activityLevel: Number(activityLevel),
      fitnessGoal,
    };
    try {
      const res = await fetch(`http://localhost:9090/api/auth/user/${user.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedUser),
      });
      const data = await res.json();
      if (res.ok) {
        setMessage('Profile updated! Redirecting...');
        onDetailsSubmit(data);
        setTimeout(() => navigate('/recommendations'), 1000);
      } else {
        setMessage(data.error || 'Failed to update profile');
      }
    } catch (err) {
      setMessage('Network error');
    }
  };

  return (
    <div className="min-h-screen flex flex-col justify-center items-center bg-gradient-to-b from-blue-50 to-white">
      <div className="w-full max-w-md p-8 rounded-2xl shadow-2xl bg-white/80 backdrop-blur-md border border-white/40 mt-16">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-900 drop-shadow">Complete Your Profile</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input type="number" placeholder="Age" value={age} onChange={e => setAge(e.target.value)} className="border p-2 rounded" required />
          <input type="number" placeholder="Height (cm)" value={height} onChange={e => setHeight(e.target.value)} className="border p-2 rounded" required />
          <input type="number" placeholder="Weight (kg)" value={weight} onChange={e => setWeight(e.target.value)} className="border p-2 rounded" required />
          <select value={activityLevel} onChange={e => setActivityLevel(Number(e.target.value))} className="border p-2 rounded">
            <option value={1}>Sedentary</option>
            <option value={2}>Lightly active</option>
            <option value={3}>Moderately active</option>
            <option value={4}>Very active</option>
            <option value={5}>Super active</option>
          </select>
          <select value={fitnessGoal} onChange={e => setFitnessGoal(e.target.value)} className="border p-2 rounded">
            <option value="lose">Lose Weight</option>
            <option value="maintain">Maintain</option>
            <option value="gain">Gain Weight</option>
          </select>
          <button type="submit" className="bg-blue-600 text-white py-2 rounded shadow hover:bg-blue-700 transition">Save & Continue</button>
        </form>
        {message && <div className="mt-4 text-red-600 text-center">{message}</div>}
      </div>
    </div>
  );
};

export default DetailsPage; 