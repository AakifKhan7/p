import { useEffect, useState, useRef } from "react";

// Guard to avoid duplicate initial fetch in React StrictMode dev re-mount
let initialSalesLoaded = false;
import { PlusCircle, Trash2, ShoppingCart, Download } from "lucide-react";
import InputField from "../components/InputField";
import NavBar from "../components/NavBar";
import { apiFetch, apiFetchBlob } from "../utils/api";

const emptyForm = {
  productName: "",
  quantity: "",
  shopId: "",
};

export default function SalesPage() {
  const [sales, setSales] = useState([]);
  const [shops, setShops] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [loading, setLoading] = useState(false);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [downloading, setDownloading] = useState(false);

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
      let url = "/api/sales/list";
      const params = [];
      if (startDate) params.push(`startDate=${startDate}`);
      if (endDate) params.push(`endDate=${endDate}`);
      if (params.length) url += "?" + params.join("&");
      const res = await apiFetch(url);
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

  const handleDownloadPdf = async () => {
    try {
      setDownloading(true);
      let url = "/api/sales/report/pdf";
      const params = [];
      if (startDate) params.push(`startDate=${startDate}`);
      if (endDate) params.push(`endDate=${endDate}`);
      if (params.length) url += "?" + params.join("&");
      const blob = await apiFetchBlob(url, {
        headers: { Accept: "application/pdf" },
      });
      const blobUrl = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = blobUrl;
      link.download = "sales_report.pdf";
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(blobUrl);
    } catch (e) {
      console.error(e);
      alert("Failed to download PDF");
    } finally {
      setDownloading(false);
    }
  };

  const getShopName = (id) => {
    const shop = shops.find((s) => s.id === id);
    return shop ? shop.name : "";
  };

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
          productName: form.productName,
          shopId: form.shopId,
          quantity: parseInt(form.quantity, 10),
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
            label="Product Name"
            name="productName"
            value={form.productName}
            onChange={handleChange}
            placeholder="Enter product name"
          />
          <InputField
            label="Quantity"
            name="quantity"
            type="number"
            value={form.quantity}
            onChange={handleChange}
            min="1"
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
            <ShoppingCart size={20} /> Recent Sales
          </h2>

          {/* Date Range Filter */}
          <div className="flex flex-col md:flex-row gap-4">
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">Start Date</label>
              <input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                className="border border-gray-300 rounded-md py-2 px-3 text-sm focus:outline-none focus:border-yellow-500"
              />
            </div>
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">End Date</label>
              <input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                className="border border-gray-300 rounded-md py-2 px-3 text-sm focus:outline-none focus:border-yellow-500"
              />
            </div>
            <button
              type="button"
              onClick={loadSales}
              className="self-end bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded-md text-sm"
            >
              Apply
            </button>
            <button
              type="button"
              onClick={handleDownloadPdf}
              disabled={downloading}
              className="self-end bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded-md text-sm flex items-center gap-1"
            >
              <Download size={14} /> {downloading ? "Downloading..." : "Download PDF"}
            </button>
          </div>

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
                      alt={sale.productName}
                      className="w-full h-40 object-cover rounded-md"
                    />
                  )}
                  <h3 className="font-medium text-gray-800">{sale.name}</h3>
                  <p className="text-sm text-gray-600">Quantity: {sale.quantity}</p>
                  <p className="text-sm text-gray-600">Price: ₹{sale.price}</p>
                  <p className="text-sm text-gray-600">Shop: {getShopName(sale.shopId)}</p>

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
