import React, { useState } from 'react';

const pushPullLegsPlan = [
  {
    day: 'Day 1: Push (Chest, Shoulders, Triceps)',
    exercises: [
      'Bench Press: 4 sets of 8-12 reps',
      'Overhead Press: 3 sets of 8-12 reps',
      'Incline Dumbbell Press: 3 sets of 10 reps',
      'Triceps Dips: 3 sets to failure',
      'Lateral Raises: 3 sets of 15 reps',
    ],
  },
  {
    day: 'Day 2: Pull (Back, Biceps)',
    exercises: [
      'Deadlift: 4 sets of 6-8 reps',
      'Pull-ups: 3 sets to failure',
      'Barbell Rows: 3 sets of 10 reps',
      'Face Pulls: 3 sets of 15 reps',
      'Bicep Curls: 3 sets of 12 reps',
    ],
  },
  {
    day: 'Day 3: Legs (Quads, Hamstrings, Calves)',
    exercises: [
      'Squats: 4 sets of 8-12 reps',
      'Leg Press: 3 sets of 12 reps',
      'Romanian Deadlift: 3 sets of 10 reps',
      'Leg Curls: 3 sets of 12 reps',
      'Standing Calf Raises: 4 sets of 15 reps',
    ],
  },
  {
    day: 'Day 4: Push (Chest, Shoulders, Triceps)',
    exercises: [
      'Incline Bench Press: 4 sets of 8-12 reps',
      'Arnold Press: 3 sets of 10 reps',
      'Chest Flyes: 3 sets of 12 reps',
      'Triceps Pushdowns: 3 sets of 12 reps',
      'Front Raises: 3 sets of 15 reps',
    ],
  },
  {
    day: 'Day 5: Pull (Back, Biceps)',
    exercises: [
      'Barbell Deadlift: 4 sets of 6-8 reps',
      'Chin-ups: 3 sets to failure',
      'Seated Cable Rows: 3 sets of 10 reps',
      'Rear Delt Flyes: 3 sets of 15 reps',
      'Hammer Curls: 3 sets of 12 reps',
    ],
  },
  {
    day: 'Day 6: Legs (Quads, Hamstrings, Calves)',
    exercises: [
      'Front Squats: 4 sets of 8-12 reps',
      'Leg Extensions: 3 sets of 12 reps',
      'Hamstring Curls: 3 sets of 12 reps',
      'Standing Calf Raises: 4 sets of 15 reps',
      'Walking Lunges: 3 sets of 20 steps',
    ],
  },
];

const Recommendations = ({ report, user }: { report: any, user?: any }) => {
  const [showPlan, setShowPlan] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleGetPlan = () => {
    setShowPlan(true);
  };

  return (
    <div className="max-w-xl mx-auto p-6 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Your Fitness Recommendations</h2>
      <div className="mb-4">
        <strong>Maintenance Calories:</strong> <span className="text-blue-700 font-semibold">{report.maintenanceCalories}</span>
      </div>
      <div className="mb-4">
        <strong>Gym Tips:</strong>
        <ul className="list-disc list-inside text-green-700">
          {report.gymTips && report.gymTips.map((tip: string, idx: number) => (
            <li key={idx}>{tip}</li>
          ))}
        </ul>
      </div>
      <button
        onClick={handleGetPlan}
        className="bg-gradient-to-r from-pink-500 to-yellow-500 text-white py-2 px-4 rounded shadow hover:scale-105 transition-transform mb-4 w-full"
        disabled={loading}
      >
        {loading ? 'Loading Plan...' : 'Get Personalized Push Pull Legs Plan'}
      </button>
      {error && <div className="text-red-600 mt-2">{error}</div>}
      {showPlan && (
        <div className="mt-6">
          <h3 className="text-xl font-bold mb-2 text-pink-700">Push Pull Legs Split (6 Days/Week)</h3>
          <div className="mb-4 text-gray-700">
            <strong>How it works:</strong> This split alternates push, pull, and leg days twice per week. Rest on Day 7. Focus on progressive overload, good form, and recovery.
          </div>
          {pushPullLegsPlan.map((split, idx) => (
            <div key={idx} className="mb-4 p-3 bg-pink-50 rounded">
              <div className="font-semibold text-purple-800 mb-1">{split.day}</div>
              <ul className="list-disc list-inside ml-4">
                {split.exercises.map((ex, i) => <li key={i}>{ex}</li>)}
              </ul>
            </div>
          ))}
          <div className="mt-4 text-gray-600">
            <strong>Tips:</strong> Warm up before each session, stretch after, and listen to your body. Adjust weights and reps as needed for your level.
          </div>
        </div>
      )}
    </div>
  );
};

export default Recommendations; 