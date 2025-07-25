import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from './Footer';
import { User } from './UserContext';

const FITNESS_BG =
  'https://images.unsplash.com/photo-1517960413843-0aee8e2d471c?auto=format&fit=crop&w=1500&q=80';

interface RegisterProps {
  onRegister: (user: User) => void;
}

const Register: React.FC<RegisterProps> = ({ onRegister }) => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [gender, setGender] = useState('male');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:9090/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username,
          email,
          password,
          gender
        })
      });
      const data = await response.json();
      if (response.ok && data.userId) {
        setMessage('Registration successful! You can now log in.');
        setTimeout(() => navigate('/login'), 1500);
      } else {
        setMessage(data.error || 'Registration failed');
      }
    } catch (err) {
      setMessage('Network error');
    }
  };

  return (
    <div className="min-h-screen flex flex-col justify-center items-center relative overflow-hidden">
      {/* Background image */}
      <div
        className="absolute inset-0 w-full h-full bg-cover bg-center z-0"
        style={{
          backgroundImage: `url(${FITNESS_BG})`,
          filter: 'blur(8px) brightness(0.7)',
        }}
        aria-hidden="true"
      />
      {/* Glassmorphism card */}
      <div className="relative z-10 w-full max-w-md mx-auto p-8 rounded-2xl shadow-2xl bg-white/30 backdrop-blur-md border border-white/40 mt-16">
        <div className="mb-6 text-center">
          <span className="block text-3xl font-bold text-blue-700 mb-2">👋 Welcome to BeastXfitness AI!</span>
          <span className="block text-lg text-gray-700">Create your account to get started.</span>
        </div>
        <h2 className="text-3xl font-bold mb-6 text-center text-gray-900 drop-shadow">Register</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input type="text" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} className="border p-2 rounded bg-white/60 focus:bg-white/80" required />
          <input type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} className="border p-2 rounded bg-white/60 focus:bg-white/80" required />
          <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} className="border p-2 rounded bg-white/60 focus:bg-white/80" required />
          <select value={gender} onChange={e => setGender(e.target.value)} className="border p-2 rounded bg-white/60 focus:bg-white/80" required>
            <option value="male">Male</option>
            <option value="female">Female</option>
            <option value="others">Others</option>
          </select>
          <button type="submit" className="bg-blue-600 text-white py-2 rounded shadow hover:bg-blue-700 transition">Register</button>
        </form>
        {message && <div className="mt-4 text-red-600 text-center">{message}</div>}
        <div className="mt-4 text-center">
          <span>Already have an account? </span>
          <button className="text-blue-600 underline" onClick={() => navigate('/login')}>Login</button>
        </div>
      </div>
      {/* Footer */}
    </div>
  );
};

export default Register; 