import { useEffect, useState, useRef } from "react";

// Guard to avoid duplicate initial fetch in React StrictMode dev re-mount
let initialSalesLoaded = false;
import { PlusCircle, Trash2, ShoppingCart } from "lucide-react";
import InputField from "../components/InputField";
import NavBar from "../components/NavBar";
import { apiFetch } from "../utils/api";

const emptyForm = {
  name: "",
  description: "",
  category: "",
  quantity: "",
  price: "",
  image: "",
  shopId: "",
};

export default function SalesPage() {
  const [sales, setSales] = useState([]);
  const [shops, setShops] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(false);

  const loadShops = async () => {
    try {
      const res = await apiFetch("/api/shop/list");
      setShops(res);
      if (res.length === 1) {
        setForm((f) => ({ ...f, shopId: res[0].id }));
      }
    } catch (e) {
      console.error(e);
      alert("Failed to load shops");
    }
  };

  const loadSales = async () => {
    try {
      const res = await apiFetch("/api/sales/list");
      setSales(res);
    } catch (e) {
      console.error(e);
      alert("Failed to load sales");
    }
  };

    const didInit = useRef(false);
  useEffect(() => {
    if (didInit.current) return; // avoid double call in React StrictMode (dev)
    didInit.current = true;
    loadShops();
    if (!initialSalesLoaded) {
      loadSales();
      initialSalesLoaded = true;
    }
  }, []);

  // periodic refresh every 60 s without spamming
  useEffect(() => {
    const id = setInterval(loadSales, 60000);
    return () => clearInterval(id);
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const resetForm = () => setForm((f) => ({ ...emptyForm, shopId: f.shopId }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.shopId) {
      alert("Please select a shop");
      return;
    }
    try {
      setLoading(true);
      await apiFetch("/api/sales/create", {
        method: "POST",
        body: JSON.stringify({
          ...form,
          quantity: parseInt(form.quantity, 10),
          price: parseFloat(form.price),
        }),
      });
      resetForm();
      await loadSales();
    } catch (err) {
      alert(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this sale?")) return;
    try {
      await apiFetch(`/api/sales/${id}/delete`, {
        method: "DELETE",
      });
      await loadSales();
    } catch (e) {
      alert(e.message);
    }
  };

  return (
    <div
      className="min-h-screen flex flex-col text-gray-900"
      style={{
        backgroundImage:
          'url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0nNjAnIGhlaWdodD0nNjAnIHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2Zyc+PHBhdGggZD0nTTAgNjAgNjAgMCA2MCA2MCAwIDYwJyBmaWxsPSdub25lJyBzdHJva2U9JyNmZmU4OGQnIHN0cm9rZS13aWR0aD0nMC41Jy8+PC9zdmc+")',
        backgroundSize: "60px 60px",
        backgroundColor: "#fefce8",
      }}
    >
      <NavBar />

      <main className="flex-1 p-6 max-w-4xl mx-auto w-full flex flex-col gap-8">
        {/* Create Sale */}
        <form
          onSubmit={handleSubmit}
          className="bg-white border border-yellow-200 rounded-xl p-8 shadow-lg flex flex-col gap-6"
        >
          <h2 className="flex items-center gap-2 font-semibold text-yellow-600 text-lg">
            <PlusCircle size={20} /> Create Sale
          </h2>

          {/* Shop dropdown if multiple */}
          {shops.length > 1 && (
            <div className="flex flex-col gap-1 w-full">
              <label className="font-medium text-sm text-gray-700">Shop</label>
              <select
                name="shopId"
                value={form.shopId}
                onChange={handleChange}
                className="w-full border border-gray-300 rounded-md py-2 px-3 text-sm focus:outline-none focus:border-yellow-500"
              >
                <option value="">Select shop</option>
                {shops.map((s) => (
                  <option key={s.id} value={s.id}>
                    {s.name}
                  </option>
                ))}
              </select>
            </div>
          )}

          <InputField
            label="Name"
            name="name"
            value={form.name}
            onChange={handleChange}
            placeholder="Product name"
          />
          <InputField
            label="Category"
            name="category"
            value={form.category}
            onChange={handleChange}
          />
          <InputField
            label="Quantity"
            name="quantity"
            type="number"
            value={form.quantity}
            onChange={handleChange}
          />
          <InputField
            label="Description"
            name="description"
            value={form.description}
            onChange={handleChange}
            placeholder="Optional description"
          />
          <InputField
            label="Price"
            name="price"
            value={form.price}
            onChange={handleChange}
            placeholder="0.00"
            type="number"
          />
          <InputField
            label="Image URL"
            name="image"
            value={form.image}
            onChange={handleChange}
            placeholder="https://example.com/img.jpg"
          />

          <button
            type="submit"
            disabled={loading}
            className="self-start inline-flex items-center gap-2 bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded-md text-sm"
          >
            <ShoppingCart size={16} /> {loading ? "Saving..." : "Save"}
          </button>
        </form>

        {/* Sales List */}
        <div className="bg-white border border-yellow-200 rounded-xl p-8 shadow-lg flex flex-col gap-6">
          <h2 className="flex items-center gap-2 font-semibold text-yellow-600 text-lg">
            <ShoppingCart size={20} /> My Sales
          </h2>

          {sales.length === 0 ? (
            <p className="text-sm text-gray-500">No sales created yet.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {sales.map((sale) => (
                <div
                  key={sale.id}
                  className="border border-gray-200 rounded-md p-4 flex flex-col gap-2 relative"
                >
                  {sale.image && (
                    <img
                      src={sale.image}
                      alt={sale.name}
                      className="w-full h-40 object-cover rounded-md"
                    />
                  )}
                  <h3 className="font-medium text-gray-800">{sale.name}</h3>
                  <p className="text-sm text-gray-600 line-clamp-3">{sale.description}</p>
                  <span className="text-yellow-600 font-semibold mt-auto">₹{sale.price}</span>

                  <button
                    type="button"
                    onClick={() => handleDelete(sale.id)}
                    className="absolute top-2 right-2 text-red-500 hover:text-red-600"
                  >
                    <Trash2 size={18} />
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  );
}
