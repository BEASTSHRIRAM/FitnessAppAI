import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from './Footer';
import { User } from './UserContext';

const FITNESS_BG =
  'https://images.unsplash.com/photo-1517960413843-0aee8e2d471c?auto=format&fit=crop&w=1500&q=80';

interface LoginProps {
  onLogin: (user: User) => void;
}

const Login: React.FC<LoginProps> = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:9090/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      const data = await response.json();
      if (response.ok && data.userId) {
        setMessage('Login successful! Fetching profile...');
        fetch(`http://localhost:9090/api/auth/user/${data.userId}`)
          .then(res => {
            if (!res.ok) throw new Error('Failed to fetch user profile');
            return res.json();
          })
          .then(fullUser => {
            setTimeout(() => navigate('/dashboard'), 1000);
            onLogin(fullUser);
          })
          .catch(() => {
            setMessage('Login succeeded, but failed to fetch profile. Please try again.');
          });
      } else {
        setMessage(data.error || 'Login failed');
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
          <span className="block text-lg text-gray-700">Please login to continue.</span>
        </div>
        <h2 className="text-3xl font-bold mb-6 text-center text-gray-900 drop-shadow">Login</h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} className="border p-2 rounded bg-white/60 focus:bg-white/80" required />
          <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} className="border p-2 rounded bg-white/60 focus:bg-white/80" required />
          <button type="submit" className="bg-blue-600 text-white py-2 rounded shadow hover:bg-blue-700 transition">Login</button>
        </form>
        {message && <div className="mt-4 text-red-600 text-center">{message}</div>}
        <div className="mt-4 text-center">
          <span>Don't have an account? </span>
          <button className="text-blue-600 underline" onClick={() => navigate('/register')}>Register</button>
        </div>
      </div>
      {/* Footer */}
    </div>
  );
};

export default Login; 