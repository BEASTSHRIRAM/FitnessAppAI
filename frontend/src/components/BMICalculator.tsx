import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const BMICalculator = ({ user, onFitnessReport }) => {
  const [height, setHeight] = useState(user?.height || '');
  const [weight, setWeight] = useState(user?.weight || '');
  const [age, setAge] = useState(user?.age || '');
  const [gender, setGender] = useState(user?.gender || '');
  const [activityLevel, setActivityLevel] = useState(user?.activityLevel || 1);
  const [goal, setGoal] = useState('maintain');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:9090/api/fitness/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...user,
          height: Number(height),
          weight: Number(weight),
          age: Number(age),
          gender,
          activityLevel: Number(activityLevel),
          goal
        })
      });
      const data = await response.json();
      if (response.ok) {
        setMessage('Calculation successful!');
        onFitnessReport(data);
        setTimeout(() => navigate('/recommendations'), 1000);
      } else {
        setMessage(data.error || 'Calculation failed');
      }
    } catch (err) {
      setMessage('Network error');
    }
  };

  return (
    <div className="max-w-md mx-auto p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Fitness Calculator</h2>
      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <input type="number" placeholder="Height (cm)" value={height} onChange={e => setHeight(e.target.value)} className="border p-2 rounded" required />
        <input type="number" placeholder="Weight (kg)" value={weight} onChange={e => setWeight(e.target.value)} className="border p-2 rounded" required />
        <input type="number" placeholder="Age" value={age} onChange={e => setAge(e.target.value)} className="border p-2 rounded" required />
        <input type="text" placeholder="Gender (male/female)" value={gender} onChange={e => setGender(e.target.value)} className="border p-2 rounded" required />
        <select value={activityLevel} onChange={e => setActivityLevel(e.target.value)} className="border p-2 rounded">
          <option value={1}>Sedentary</option>
          <option value={2}>Lightly active</option>
          <option value={3}>Moderately active</option>
          <option value={4}>Very active</option>
          <option value={5}>Super active</option>
        </select>
        <select value={goal} onChange={e => setGoal(e.target.value)} className="border p-2 rounded">
          <option value="lose">Lose Weight</option>
          <option value="maintain">Maintain</option>
          <option value="gain">Gain Weight</option>
        </select>
        <button type="submit" className="bg-blue-600 text-white py-2 rounded">Calculate</button>
      </form>
      {message && <div className="mt-4 text-green-600">{message}</div>}
    </div>
  );
};

export default BMICalculator;