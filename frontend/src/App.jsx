import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./pages/Dashboard";
import CTA from "./components/CTA";
import HowItWorks from "./components/HowItWorks";
import Navbar from "./components/Navbar";
import Hero from "./components/Hero";
import Features from "./components/Features";
import Footer from "./components/Footer";
import MyPets from "./pages/MyPets";
import Appointments from "./pages/Appointments";
import PetProfile from "./pages/PetProfile";
import HealthRecords from "./pages/HealthRecords";
import AuthProvider from "./AuthProvider";
import ProtectedRoute from "./ProtectedRoute";
import { useAuth } from "./auth-context";
function Home() { return <><Navbar /><Hero /><Features /><HowItWorks /><CTA /><Footer /></>; }
function AppRoutes() {
  const { user, ready } = useAuth(); const guarded = page => <ProtectedRoute>{page}</ProtectedRoute>;
  return <Routes><Route path="/" element={<Home />} /><Route path="/login" element={ready && user ? <Navigate to="/dashboard" replace /> : <Login />} /><Route path="/register" element={ready && user ? <Navigate to="/dashboard" replace /> : <Register />} /><Route path="/dashboard" element={guarded(<Dashboard />)} /><Route path="/pets" element={guarded(<MyPets />)} /><Route path="/pets/:id" element={guarded(<PetProfile />)} /><Route path="/appointments" element={guarded(<Appointments />)} /><Route path="/health-records" element={guarded(<HealthRecords />)} /><Route path="*" element={<Navigate to="/" replace />} /></Routes>;
}
export default function App() { return <BrowserRouter><AuthProvider><AppRoutes /></AuthProvider></BrowserRouter>; }
