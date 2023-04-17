import * as React from 'react';
import TextField from '@mui/material/TextField';
import { Container , Paper } from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import { useState } from 'react';
import { Button } from '@mui/material';

const useStyles = makeStyles((theme) => ({
  root: {
    '& > *':{
      margin: theme.spacing(1),
    },
  },
}));

export default function BasicTextFields() {
const paperStyle={padding:'50px 20px',width:600,margin:"20px auto"}
const [productName,setproductName] = useState('')
const [productDescription,setproductDescription] = useState('')
const [productQuantity,setproductQuantity] = useState('')
const [productPrice,setproductPrice] = useState('')
const classes =useStyles();

const handleClick=(e)=>{
  e.preventDefault()
  const product={productName,productDescription,productQuantity,productPrice}
  console.log(product)
fetch("http://localhost:8080/product/add",{
  method:"POST",
  headers:{"Content-Type":"application/json"},
  body:JSON.stringify(product)

}).then(()=>{
  console.log("New Product added")
})
}
  return ( 
    <Container> 
      <Paper elevation={3} style={paperStyle}>
        <h1 style={{color:"blue"}}><u>Add Products</u></h1>

      <form className={classes.root} noValidate autoComplete="off">

      <TextField margin="normal" id="outlined-basic" label="Product Name" variant="outlined" fullWidth value={productName}
      onChange={(e)=>setproductName(e.target.value)}/>
      <TextField margin="normal" id="outlined-basic" label="Product Description" variant="outlined" fullWidth value={productDescription}
      onChange={(e)=>setproductDescription(e.target.value)}/>
      <TextField margin="normal" id="outlined-basic" label="Product Qty " variant="outlined" fullWidth value={productQuantity}
      onChange={(e)=>setproductQuantity(e.target.value)}/> 
      <TextField margin="normal" id="outlined-basic" label="Product Price"   variant="outlined" fullWidth value={productPrice}
      onChange={(e)=>setproductPrice(e.target.value)}/>

      <Button variant='contained' color='primary' onClick={handleClick}>
        Submit
      </Button>
    
      </form>
    
    </Paper> 
    </Container>
  );
}
