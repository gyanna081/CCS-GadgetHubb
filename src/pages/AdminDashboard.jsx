import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { db } from '../firebaseconfig';
import { collection, getDocs, query, where, doc, updateDoc } from 'firebase/firestore';

function AdminDashboard() {
  const [activeTab, setActiveTab] = useState('items');
  const [items, setItems] = useState([]);
  const [borrowRequests, setBorrowRequests] = useState([]);
  const [itemName, setItemName] = useState('');
  const [description, setDescription] = useState('');
  const [condition, setCondition] = useState('Good');
  const [image, setImage] = useState(null);
  const [editItemId, setEditItemId] = useState(null);
  const [requestStatusFilter, setRequestStatusFilter] = useState('Pending');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchItems();
    if (activeTab === 'requests') {
      fetchBorrowRequests();
    }
  }, [activeTab, requestStatusFilter]);

  const fetchItems = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/items');
      setItems(response.data);
    } catch (error) {
      console.error('Error fetching items:', error);
    }
  };

  const fetchBorrowRequests = async () => {
    setLoading(true);
    setError(null);
    try {
      const borrowRequestsRef = collection(db, 'borrowRequests');
      const q = requestStatusFilter === 'All'
        ? borrowRequestsRef
        : query(borrowRequestsRef, where('status', '==', requestStatusFilter));
      const snapshot = await getDocs(q);
      const requests = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
      setBorrowRequests(requests);
    } catch (error) {
      console.error('Error fetching borrow requests:', error);
      setError('Failed to load requests. Please try again.');
      setBorrowRequests([]);
    }
    setLoading(false);
  };

  const handleAddItem = async () => {
    if (!itemName || !description || !condition) {
      alert('Please fill out all fields.');
      return;
    }
    const formData = new FormData();
    formData.append('name', itemName);
    formData.append('description', description);
    formData.append('condition', condition);
    if (image) {
      formData.append('image', image);
    }
    try {
      await axios.post('http://localhost:8080/api/items', formData);
      setItemName('');
      setDescription('');
      setCondition('Good');
      setImage(null);
      fetchItems();
    } catch (error) {
      console.error('Error adding item:', error);
    }
  };

  const handleUpdateItem = async (item) => {
    try {
      await axios.put(`http://localhost:8080/api/items/${item.id || item.itemId}`, {
        name: item.name,
        description: item.description,
        condition: item.condition,
        status: item.status
      });
      setEditItemId(null);
      fetchItems();
    } catch (error) {
      console.error('Error updating item:', error);
    }
  };

  const handleDeleteItem = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/items/${id}`);
      fetchItems();
    } catch (error) {
      console.error('Error deleting item:', error);
    }
  };

  const handleUpdateRequestStatus = async (requestId, newStatus) => {
    try {
      const requestRef = doc(db, 'borrowRequests', requestId);
      await updateDoc(requestRef, { status: newStatus });
      fetchBorrowRequests();
    } catch (error) {
      console.error('Error updating request status:', error);
      alert('Failed to update request status.');
    }
  };

  const handleImageChange = (e) => setImage(e.target.files[0]);

  const handleInputChange = (e, field, item) => {
    const updatedItems = items.map((i) =>
      (i.id === item.id || i.itemId === item.itemId) ? { ...i, [field]: e.target.value } : i
    );
    setItems(updatedItems);
  };

  const handleLogout = () => {
    localStorage.clear();
    window.location.href = '/';
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    } catch (error) {
      return dateString;
    }
  };

  // JSX Return starts here
  return (
    <div style={{ padding: '30px' }}>
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h1>Admin Dashboard</h1>
        <button
          onClick={handleLogout}
          style={{ backgroundColor: '#e63946', color: 'white', border: 'none', padding: '10px 20px', borderRadius: '8px', cursor: 'pointer' }}
        >
          Logout
        </button>
      </div>

      {/* Tab Navigation */}
      <div style={{ marginBottom: '20px', borderBottom: '1px solid #ddd' }}>
        <button 
          onClick={() => setActiveTab('items')}
          style={{ 
            padding: '10px 20px', 
            marginRight: '10px', 
            backgroundColor: activeTab === 'items' ? '#e76f51' : '#f4f4f4',
            color: activeTab === 'items' ? 'white' : 'black',
            border: 'none',
            borderRadius: '8px 8px 0 0',
            cursor: 'pointer'
          }}
        >
          Manage Items
        </button>
        <button 
          onClick={() => setActiveTab('requests')}
          style={{ 
            padding: '10px 20px', 
            backgroundColor: activeTab === 'requests' ? '#e76f51' : '#f4f4f4',
            color: activeTab === 'requests' ? 'white' : 'black',
            border: 'none',
            borderRadius: '8px 8px 0 0',
            cursor: 'pointer'
          }}
        >
          Borrow Requests
        </button>
      </div>

      {/* Items Management Section */}
      {activeTab === 'items' && (
        <>
          <div style={{ display: 'flex', gap: '10px', marginBottom: '20px', alignItems: 'center' }}>
            <input
              type="text"
              placeholder="Item Name"
              value={itemName}
              onChange={(e) => setItemName(e.target.value)}
              style={{ padding: '8px', width: '200px' }}
            />
            <input
              type="text"
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              style={{ padding: '8px', width: '200px' }}
            />
            <select
              value={condition}
              onChange={(e) => setCondition(e.target.value)}
              style={{ padding: '8px', width: '120px' }}
            >
              <option value="Good">Good</option>
              <option value="Fair">Fair</option>
              <option value="Poor">Poor</option>
            </select>
            <input
              type="file"
              onChange={handleImageChange}
              style={{ padding: '8px' }}
            />
            <button
              onClick={handleAddItem}
              style={{ backgroundColor: '#e76f51', color: 'white', border: 'none', padding: '10px 20px', borderRadius: '8px', cursor: 'pointer' }}
            >
              Add Item
            </button>
          </div>

          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ backgroundColor: '#2c3e50', color: 'white' }}>
                <th style={{ padding: '10px' }}>ID</th>
                <th style={{ padding: '10px' }}>Name</th>
                <th style={{ padding: '10px' }}>Description</th>
                <th style={{ padding: '10px' }}>Condition</th>
                <th style={{ padding: '10px' }}>Status</th>
                <th style={{ padding: '10px' }}>Image</th>
                <th style={{ padding: '10px' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.id || item.itemId} style={{ borderBottom: '1px solid #ccc' }}>
                  <td style={{ padding: '10px', textAlign: 'center' }}>{item.id || item.itemId}</td>
                  <td style={{ padding: '10px' }}>
                    {editItemId === (item.id || item.itemId) ? (
                      <input
                        type="text"
                        value={item.name}
                        onChange={(e) => handleInputChange(e, 'name', item)}
                        style={{ padding: '5px', width: '100%' }}
                      />
                    ) : (
                      item.name
                    )}
                  </td>
                  <td style={{ padding: '10px' }}>
                    {editItemId === (item.id || item.itemId) ? (
                      <input
                        type="text"
                        value={item.description}
                        onChange={(e) => handleInputChange(e, 'description', item)}
                        style={{ padding: '5px', width: '100%' }}
                      />
                    ) : (
                      item.description
                    )}
                  </td>
                  <td style={{ padding: '10px', textAlign: 'center' }}>
                    {editItemId === (item.id || item.itemId) ? (
                      <select
                        value={item.condition}
                        onChange={(e) => handleInputChange(e, 'condition', item)}
                        style={{ padding: '5px' }}
                      >
                        <option value="Good">Good</option>
                        <option value="Fair">Fair</option>
                        <option value="Poor">Poor</option>
                      </select>
                    ) : (
                      item.condition
                    )}
                  </td>
                  <td style={{ padding: '10px', textAlign: 'center' }}>
                    {editItemId === (item.id || item.itemId) ? (
                      <select
                        value={item.status}
                        onChange={(e) => handleInputChange(e, 'status', item)}
                        style={{ padding: '5px' }}
                      >
                        <option value="Available">Available</option>
                        <option value="Borrowed">Borrowed</option>
                      </select>
                    ) : (
                      item.status
                    )}
                  </td>
                  <td style={{ padding: '10px', textAlign: 'center' }}>
                    {item.imagePath ? (
                      <img
                      src={
                        item.imagePath?.startsWith('http')
                          ? item.imagePath
                          : `http://localhost:8080/${item.imagePath}`
                      }
                      alt={item.name || "Item"}
                      style={{
                        width: '50px',
                        height: '50px',
                        objectFit: 'cover',
                        border: '1px solid #ddd',
                        borderRadius: '4px'
                      }}
                      onError={(e) => {
                        console.warn(`Image failed to load: ${item.imagePath}`);
                        e.target.onerror = null;
                        const name = item.name || "Item";
                        const initial = name.charAt(0).toUpperCase();
                        const colors = ['#3498db', '#e74c3c', '#2ecc71', '#f39c12', '#9b59b6', '#1abc9c'];
                        const bgColor = colors[Math.abs(name.charCodeAt(0)) % colors.length];
                    
                        const canvas = document.createElement('canvas');
                        canvas.width = 50;
                        canvas.height = 50;
                        const ctx = canvas.getContext('2d');
                        ctx.fillStyle = bgColor;
                        ctx.fillRect(0, 0, 50, 50);
                        ctx.fillStyle = '#ffffff';
                        ctx.font = 'bold 24px Arial';
                        ctx.textAlign = 'center';
                        ctx.textBaseline = 'middle';
                        ctx.fillText(initial, 25, 25);
                    
                        e.target.src = canvas.toDataURL();
                      }}
                    />
                    
                    ) : (
                      <div style={{ 
                        width: '50px', 
                        height: '50px', 
                        backgroundColor: '#6c757d', 
                        color: 'white',
                        display: 'flex', 
                        alignItems: 'center', 
                        justifyContent: 'center', 
                        margin: '0 auto', 
                        border: '1px solid #ddd', 
                        borderRadius: '4px', 
                        fontSize: '16px',
                        fontWeight: 'bold' 
                      }}>
                        {(item.name?.charAt(0) || 'I').toUpperCase()}
                      </div>
                    )}
                  </td>
                  <td style={{ padding: '10px', textAlign: 'center' }}>
                    {editItemId === (item.id || item.itemId) ? (
                      <>
                        <button 
                          onClick={() => handleUpdateItem(item)} 
                          style={{ marginRight: '5px', padding: '5px 10px', backgroundColor: '#4CAF50', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                        >
                          Save
                        </button>
                        <button 
                          onClick={() => setEditItemId(null)} 
                          style={{ padding: '5px 10px', backgroundColor: '#607D8B', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                        >
                          Cancel
                        </button>
                      </>
                    ) : (
                      <>
                        <button 
                          onClick={() => setEditItemId(item.id || item.itemId)} 
                          style={{ marginRight: '5px', padding: '5px 10px', backgroundColor: '#FF9800', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                        >
                          Edit
                        </button>
                        <button 
                          onClick={() => handleDeleteItem(item.id || item.itemId)} 
                          style={{ padding: '5px 10px', backgroundColor: '#F44336', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                        >
                          Delete
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}

      {/* Borrow Requests Section */}
      {activeTab === 'requests' && (
        <>
          <div style={{ marginBottom: '20px', display: 'flex', alignItems: 'center' }}>
            <h2 style={{ margin: 0 }}>Manage Borrow Requests</h2>
            <div style={{ marginLeft: 'auto', display: 'flex', alignItems: 'center' }}>
              <label style={{ marginRight: '10px' }}>Filter by Status:</label>
              <select 
                value={requestStatusFilter}
                onChange={(e) => setRequestStatusFilter(e.target.value)}
                style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ddd' }}
              >
                <option value="All">All Requests</option>
                <option value="Pending">Pending</option>
                <option value="Approved">Approved</option>
                <option value="Rejected">Rejected</option>
                <option value="Returned">Returned</option>
              </select>
              <button 
                onClick={fetchBorrowRequests}
                style={{ 
                  marginLeft: '10px',
                  padding: '8px 15px',
                  backgroundColor: '#6c757d',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}
              >
                Refresh
              </button>
            </div>
          </div>

          {loading ? (
            <div style={{ 
              padding: '20px', 
              textAlign: 'center',
              backgroundColor: '#f8f9fa',
              borderRadius: '8px'
            }}>
              <p>Loading requests...</p>
            </div>
          ) : error ? (
            <div style={{ 
              padding: '20px', 
              textAlign: 'center',
              backgroundColor: '#f8d7da',
              color: '#721c24',
              borderRadius: '8px',
              border: '1px solid #f5c6cb'
            }}>
              <p>{error}</p>
            </div>
          ) : borrowRequests.length === 0 ? (
            <div style={{ 
              padding: '20px', 
              textAlign: 'center', 
              backgroundColor: '#f8f9fa',
              borderRadius: '8px',
              border: '1px solid #ddd'
            }}>
              <p>No {requestStatusFilter.toLowerCase() !== 'all' ? requestStatusFilter.toLowerCase() : ''} borrow requests found.</p>
            </div>
          ) : (
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ backgroundColor: '#2c3e50', color: 'white' }}>
                  <th style={{ padding: '10px' }}>Item Name</th>
                  <th style={{ padding: '10px' }}>Requested By</th>
                  <th style={{ padding: '10px' }}>Request Date</th>
                  <th style={{ padding: '10px' }}>Borrow Period</th>
                  <th style={{ padding: '10px' }}>Purpose</th>
                  <th style={{ padding: '10px' }}>Status</th>
                  <th style={{ padding: '10px' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {borrowRequests.map((request) => {
                  const itemName = request.itemName || 'Unknown Item';
                  const userName = request.borrowerName || request.userName || request.borrowerId || request.userId || 'Unknown User';
                  const startDate = request.startDate || '';
                  const endDate = request.endDate || '';
                  
                  return (
                    <tr key={request.id} style={{ 
                      borderBottom: '1px solid #ccc',
                      backgroundColor: request.status === 'Pending' ? '#fff3cd' : 
                                      request.status === 'Approved' ? '#d1e7dd' :
                                      request.status === 'Rejected' ? '#f8d7da' : '#ffffff'
                    }}>
                      <td style={{ padding: '10px' }}>{itemName}</td>
                      <td style={{ padding: '10px' }}>{userName}</td>
                      <td style={{ padding: '10px' }}>{formatDate(request.requestDate)}</td>
                      <td style={{ padding: '10px' }}>
                        {startDate && endDate ? `${formatDate(startDate)} to ${formatDate(endDate)}` : 'Not specified'}
                      </td>
                      <td style={{ padding: '10px' }}>{request.purpose || 'Not specified'}</td>
                      <td style={{ padding: '10px' }}>
                        <div style={{ 
                          display: 'inline-block',
                          padding: '5px 10px',
                          borderRadius: '20px',
                          fontSize: '14px',
                          fontWeight: 'bold',
                          backgroundColor: 
                            request.status === 'Pending' ? '#ffc107' : 
                            request.status === 'Approved' ? '#28a745' : 
                            request.status === 'Rejected' ? '#dc3545' : 
                            request.status === 'Returned' ? '#17a2b8' : '#6c757d',
                          color: 'white'
                        }}>
                          {request.status}
                        </div>
                      </td>
                      <td style={{ padding: '10px', textAlign: 'center' }}>
                        {request.status === 'Pending' && (
                          <>
                            <button 
                              onClick={() => handleUpdateRequestStatus(request.id, 'Approved')}
                              style={{ 
                                marginRight: '5px', 
                                padding: '5px 10px', 
                                backgroundColor: '#28a745', 
                                color: 'white', 
                                border: 'none', 
                                borderRadius: '4px', 
                                cursor: 'pointer' 
                              }}
                            >
                              Approve
                            </button>
                            <button 
                              onClick={() => handleUpdateRequestStatus(request.id, 'Rejected')}
                              style={{ 
                                padding: '5px 10px', 
                                backgroundColor: '#dc3545', 
                                color: 'white', 
                                border: 'none', 
                                borderRadius: '4px', 
                                cursor: 'pointer' 
                              }}
                            >
                              Reject
                            </button>
                          </>
                        )}
                        {request.status === 'Approved' && (
                          <button 
                            onClick={() => handleUpdateRequestStatus(request.id, 'Returned')}
                            style={{ 
                              padding: '5px 10px', 
                              backgroundColor: '#17a2b8', 
                              color: 'white', 
                              border: 'none', 
                              borderRadius: '4px', 
                              cursor: 'pointer' 
                            }}
                          >
                            Mark Returned
                          </button>
                        )}
                        <button 
                          onClick={() => alert(`View details for request ${request.id}`)}
                          style={{ 
                            marginLeft: request.status === 'Pending' ? '5px' : '0',
                            padding: '5px 10px', 
                            backgroundColor: '#6c757d', 
                            color: 'white', 
                            border: 'none', 
                            borderRadius: '4px', 
                            cursor: 'pointer' 
                          }}
                        >
                          View
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </>
      )}
    </div>
  );
}

export default AdminDashboard;