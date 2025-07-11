# 📦 Dukandar Digital Hisab App (Voice-based Sale Logger)

A Progressive Web App (PWA) for local shopkeepers to replace handwritten notebooks with a smart voice-entry system.
This version supports **manual admin-based activation**, **trial system**, and basic sale tracking/reporting features.

---

## Test Changes By Benzo Cv

🛠️ Tech Stack

- **Frontend:** React + Tailwind + Vite (PWA enabled)
- **Backend:** Node.js + Express (or Firebase Functions / Supabase Edge Functions)
- **Database:** Supabase / Firebase Firestore
- **Authentication:** Phone OTP (Firebase Auth or Supabase Auth)
- **Storage:** Cloudinary / Firebase Storage for PDF reports

---

## ✅ Core Features

| Feature                      | Description                                       |
| ---------------------------- | ------------------------------------------------- |
| 🔐 Phone-based Login         | OTP-based simple login system                     |
| 🎙️ Voice Sale Entry        | Speak item & amount → auto-log sale              |
| 📊 Daily Dashboard           | Summary of today's sale, chart/graph              |
| 🧾 PDF Report Generator      | Download or send weekly/monthly report            |
| 🧮 Sales History             | Full list of past entries with filter             |
| 🕒 Trial Countdown           | 7-day free usage, then manual activation required |
| 💰 UPI-based Payment         | Manual payment via UPI                            |
| 👑 Admin Panel (Manual Only) | Manually activate/deactivate user from backend    |

---

## 🔗 Frontend Pages (Routes)

| Path              | Purpose                                  |
| ----------------- | ---------------------------------------- |
| `/login`        | Phone OTP login                          |
| `/dashboard`    | Show summary, today’s sales             |
| `/entry`        | Voice/text-based entry input             |
| `/sales`        | History of all sales                     |
| `/report`       | Generate and download reports            |
| `/trial-status` | Show remaining trial days or payment msg |
| `/settings`     | Update name / store name                 |
| `/admin/users`  | Admin dashboard to manage users          |

---

## 🔧 API Endpoints (REST)

### 🔐 Auth

- `POST /api/auth/send-otp` → Send OTP to phone
- `POST /api/auth/verify-otp` → Verify OTP and return token
- `GET  /api/auth/me` → Get current user

### 📊 Dashboard

- `GET /api/dashboard/summary` → Today’s summary
- `GET /api/dashboard/chart` → Weekly sale chart

### 🎙️ Sale Entry

- `POST /api/sales/create` → Create sale entry
- `GET  /api/sales/list` → List all sales
- `DELETE /api/sales/:id/delete` → (Optional) Delete sale

### 📄 Reports

- `POST /api/report/generate` → Create PDF for selected dates
- `POST /api/report/send` → Send report to WhatsApp or download link

### 🔒 User

- `GET /api/user/status` → Get current trial/active status
- `GET /api/user/profile` → Get user info
- `POST /api/user/profile/update` → Update profile info

### 👑 Admin Only

- `GET /api/admin/users` → List all users
- `POST /api/admin/users/create` → Manually register new user
- `POST /api/admin/users/:id/activate` → Activate user after payment
- `POST /api/admin/users/:id/deactivate` → Deactivate user
- `GET /api/admin/users/search?phone=xxxx` → Find user by phone

---

## 🕒 Trial System

- On registration, user gets 7-day free trial (`createdAt + 7 days`)
- After 7 days, system auto blocks sales/report access
- User shown a message: "Trial over. Please pay ₹100 via UPI to activate."

---

## 💰 Manual Payment Flow

1. User registers → Trial starts
2. UPI message shown: `Pay ₹100 to bhai@upi`
3. User sends proof to you (WhatsApp/SMS)
4. You activate from admin panel

---

## 🧑‍💼 Admin Panel Usage

- Built-in access with `/admin/users`
- Search by phone
- View user trial status
- One-click: ✅ Activate / ⛔ Deactivate

---

## 🚀 How to Run (Dev Mode)

```bash
# Frontend
cd frontend
npm install
npm run dev

# Backend (if using Express)
cd backend
npm install
npm run dev
```
