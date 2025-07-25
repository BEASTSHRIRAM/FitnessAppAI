import React from 'react';
import { useUser } from './UserContext';
import { FaUser, FaEnvelope, FaVenusMars, FaBirthdayCake, FaRulerVertical, FaWeight, FaRunning, FaBullseye, FaHeartbeat, FaFireAlt, FaDumbbell, FaEdit } from 'react-icons/fa';

const iconMap: Record<string, JSX.Element> = {
  Username: <FaUser color="#3b82f6" size={24} />,
  Email: <FaEnvelope color="#3b82f6" size={24} />,
  Gender: <FaVenusMars color="#3b82f6" size={24} />,
  Age: <FaBirthdayCake color="#3b82f6" size={24} />,
  'Height (cm)': <FaRulerVertical color="#3b82f6" size={24} />,
  'Weight (kg)': <FaWeight color="#3b82f6" size={24} />,
  'Activity Level': <FaRunning color="#3b82f6" size={24} />,
  'Fitness Goal': <FaBullseye color="#3b82f6" size={24} />,
  BMI: <FaHeartbeat color="#3b82f6" size={24} />,
  'Calorie Needs': <FaFireAlt color="#3b82f6" size={24} />,
  'Workout Split': <FaDumbbell color="#3b82f6" size={24} />,
};

interface DashboardProps {
  onEditProfile?: () => void;
}

const Dashboard: React.FC<DashboardProps> = ({ onEditProfile }) => {
  const { user } = useUser();
  if (!user) return null;

  const infoCards = [
    { label: 'Username', value: user.username },
    { label: 'Email', value: user.email },
    { label: 'Gender', value: user.gender },
    { label: 'Age', value: user.age },
    { label: 'Height (cm)', value: user.height },
    { label: 'Weight (kg)', value: user.weight },
    { label: 'Activity Level', value: user.activityLevel },
    { label: 'Fitness Goal', value: user.fitnessGoal },
    { label: 'BMI', value: user.bmi },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-100 to-blue-100 py-12 px-2 flex flex-col items-center">
      <h1 className="text-3xl font-bold mb-8 text-center text-gray-800 drop-shadow">Profile Dashboard</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 w-full max-w-4xl mb-8">
        {infoCards.map(card => (
          <div key={card.label} className="bg-white/80 backdrop-blur rounded-xl shadow p-6 flex flex-col items-center border border-gray-200 min-w-[140px] min-h-[120px]">
            <div className="mb-2">{iconMap[card.label]}</div>
            <span className="text-gray-500 text-xs uppercase tracking-wider mb-1">{card.label}</span>
            <span className="text-lg font-semibold text-gray-900 break-words text-center">{card.value ?? '-'}</span>
          </div>
        ))}
      </div>
      <button
        className="px-6 py-2 bg-blue-600 text-white rounded-lg shadow hover:bg-blue-700 transition flex items-center gap-2"
        onClick={onEditProfile}
      >
        <FaEdit /> Edit Profile
      </button>
    </div>
  );
};

export default Dashboard; 