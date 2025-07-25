import React, { createContext, useContext, useState, ReactNode } from 'react';

export interface User {
  id?: string;
  username: string;
  email: string;
  gender: string;
  age?: number;
  height?: number;
  weight?: number;
  activityLevel?: number;
  fitnessGoal?: string;
  bmi?: number;
}

interface UserContextType {
  user: User | null;
  setUser: (user: User | null) => void;
  logout: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const logout = () => setUser(null);
  return (
    <UserContext.Provider value={{ user, setUser, logout }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  const context = useContext(UserContext);
  if (!context) throw new Error('useUser must be used within a UserProvider');
  return context;
}; 