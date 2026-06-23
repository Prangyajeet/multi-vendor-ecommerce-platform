import Sidebar from "../components/sidebar/Sidebar";

function VendorLayout({ children }) {

  const menuItems = [
    "Dashboard",
    "Products",
    "Inventory",
    "Orders",
    "Revenue",
  ];

  return (
    <div className="flex">

      <Sidebar menuItems={menuItems} />

      <main className="flex-1 p-6">
        {children}
      </main>

    </div>
  );
}

export default VendorLayout;