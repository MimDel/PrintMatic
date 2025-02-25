import React from 'react'

const Button = ({title}) => {
  return (
    <div className='bg-primary text-md text-white shadow-custom px-10 py-1 cursor-pointer text-center hover:bg-blue-900'>
      {title}
    </div>
  )
}

export default Button