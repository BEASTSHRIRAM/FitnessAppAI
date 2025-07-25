import React from 'react';
import { Activity } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { FaUserCircle } from 'react-icons/fa';

interface HeaderProps {
  user?: any;
  onProfileClick?: () => void;
}

const Header: React.FC<HeaderProps> = ({ user, onProfileClick }) => {
  const navigate = useNavigate();
  return (
    <header className="bg-blue-600 text-white shadow-md">
      <div className="container mx-auto px-4 py-4 flex items-center justify-between">
        <button
          className="flex items-center focus:outline-none"
          onClick={() => navigate('/homepage')}
          style={{ background: 'none', border: 'none', padding: 0, margin: 0, cursor: 'pointer' }}
        >
          <Activity className="h-8 w-8 mr-2" />
          <h1 className="text-2xl font-bold">BeastXfitness AI</h1>
        </button>
        {user && (
          <button
            className="text-blue-100 hover:text-white focus:outline-none"
            onClick={onProfileClick}
            aria-label="Open profile dashboard"
            style={{ background: 'none', border: 'none', padding: 0, margin: 0, cursor: 'pointer' }}
          >
            <FaUserCircle size={36} />
          </button>
        )}
      </div>
    </header>
  );
};

export default Header;