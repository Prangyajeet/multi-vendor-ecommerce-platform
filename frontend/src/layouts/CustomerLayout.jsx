import Navbar from "../components/navbar/Navbar";

function CustomerLayout({ children }) {
  return (
    <div>

      <Navbar />

      <main className="p-6">
        {children}
      </main>

    </div>
  );
}

export default CustomerLayout;