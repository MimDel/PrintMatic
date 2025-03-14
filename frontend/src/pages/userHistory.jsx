import { FaCheckCircle, FaHourglassHalf, FaClock, FaTimesCircle, FaMoneyBillWave, FaMoneyBill } from "react-icons/fa";
import HeaderUser from "../components/headerUser";
import { useEffect, useState } from "react";
import { getUserOrders } from "../services/orderService";

export default function UserHistory() {

  const [orders, setOrders] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState(null)
  const pageSize = 4;

  const getStatusIcon = (status) => {
    switch (status) {
      case 'UNPAID':
        return <FaMoneyBill className="text-red-500 text-md" />
      case 'COMPLETED':
        return <FaCheckCircle className="text-green-500 text-md" />;
      case 'IN_PROGRESS':
        return <FaHourglassHalf className="text-blue-500 text-md" />;
      case 'PENDING':
        return <FaClock className="text-yellow-500 text-md" />;
      case 'CANCELED':
        return <FaTimesCircle className="text-red-500 text-md" />;
      case 'REJECTED':
        return <FaTimesCircle className="text-orange-500 text-md" />;
      case 'REFUNDED':
        return <FaMoneyBillWave className="text-green-600 text-md" />;
      default:
        return null;
    }
  };

  const fetchOrders = async (page) => {
    setIsLoading(true)
    try {
      const response = await getUserOrders(page, pageSize)
      setOrders(response.content)
      setTotalPages(response.totalPages)
    } catch (error) {
      setError(error.message)
      console.log(error)
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchOrders(currentPage)
  }, [currentPage])

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage)
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('bg-BG')
  }

  const formatPrice = (number) => {
    return new Intl.NumberFormat('bg-BG', {
      style: 'currency',
      currency: 'BGN'
    }).format(number);
  }

  if (isLoading) {
    return (
      <div className="w-screen h-screen flex flex-row hover:cursor-default">
        <HeaderUser />
        <div className="w-full h-screen flex justify-center items-center">
          <p className="text-2xl">Зареждане...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="w-screen h-screen flex flex-row hover:cursor-default">
        <HeaderUser />
        <div className="w-full h-screen flex justify-center items-center">
          <p className="text-2xl text-red-500">Грешка: {error}</p>
        </div>
      </div>
    );
  }


  return (
    <div className="w-screen h-screen flex flex-row hover:cursor-default">
      <HeaderUser />
      <div className="w-full h-screen overflow-scroll flex flex-col p-12">
        <p className="text-5xl font-bold">Минали заявки</p>
        <div className="flex flex-row flex-wrap mt-12">
          {orders.length === 0 ? (<p className="text-lg text-gray-500">Нямате минали заявки</p>)
            :
            (orders.map((order) => (
              <a
                key={order.id}
                className="w-1/3 min-h-16 bg-gray-200 flex flex-col p-4 rounded-lg m-4 hover:scale-[1.02] duration-300 transition-all"
                href={order.documentUrl}
              >
                <img src={"/images/pdf.png"} alt={order.title} className="w-12" />
                <p className="text-md font-bold">{order.title}</p>
                <div className="flex flex-row gap-1 items-center">
                  <p className="text-md">Статус: {
                    order.status === 'UNPAID' ? 'Неплатена' :
                      order.status === 'PENDING' ? 'Изчакващ' :
                        order.status === 'IN_PROGRESS' ? 'В процес' :
                          order.status === 'COMPLETED' ? 'Завършена' :
                            order.status === 'CANCELED' ? 'Отказана' :
                              order.status === 'REJECTED' ? 'Отхвърлена' :
                                order.status === 'REFUNDED' ? 'Възстановена' :
                                  order.status
                  }</p>
                  {getStatusIcon(order.status)}
                </div>
                <p className="text-md">Дата на поръчка: {formatDate(order.createdAt)}</p>
                <p className="text-md">Крайна дата за изпълнение: {formatDate(order.deadline)}</p>
                <p className="text-md">Цена: {formatPrice(order.price)}</p>
              </a>
            )))}
        </div>

        {totalPages > 1 && (
          <div className="flex justify-center mt-8 gap-2 pb-8">
            <button
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 0}
              className="px-4 py-2 bg-gray-200 rounded-lg disabled:opacity-50 hover:bg-gray-300 transition-colors"
            >
              Предишна
            </button>

            {[...Array(totalPages)].map((_, index) => (
              <button
                key={index}
                onClick={() => handlePageChange(index)}
                className={`px-4 py-2 rounded-lg transition-colors ${currentPage === index
                  ? 'bg-primary text-white'
                  : 'bg-gray-200 hover:bg-gray-300'
                  }`}
              >
                {index + 1}
              </button>
            ))}

            <button
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages - 1}
              className="px-4 py-2 bg-gray-200 rounded-lg disabled:opacity-50 hover:bg-gray-300 transition-colors"
            >
              Следваща
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
