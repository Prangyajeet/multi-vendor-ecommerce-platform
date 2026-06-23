function Modal({ children }) {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50">

      <div className="bg-white p-6 rounded-xl">
        {children}
      </div>

    </div>
  );
}

export default Modal;