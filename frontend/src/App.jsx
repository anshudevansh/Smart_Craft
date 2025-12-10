import { useState } from "react";
import "./App.css";
import axios from "axios";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Box,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  TextField,
  Select,
  CircularProgress,
  Paper,
} from "@mui/material";

function App() {
  const [emailContent, setEmailContent] = useState("");
  const [tone, setTone] = useState("");
  const [generatedReply, setGeneratedReply] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async () => {
    setLoading(true);
    setError("");
    try {
      const response = await axios.post("http://localhost:8080/api/email/generate", {
        emailContent,
        tone,
      });
      setGeneratedReply(
        typeof response.data === "string"
          ? response.data
          : JSON.stringify(response.data)
      );
    } catch (error) {
      setError("Unable to generate response: Sorry for the inconvenience caused!!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      {/* Navbar
      <AppBar position="static" sx={{ background: "rgba(0,0,0,0.3)", boxShadow: "none" }}>
        <Toolbar sx={{ justifyContent: "space-between" }}>
          <Typography variant="h6" sx={{ fontWeight: "bold" }}>
            SmartCraft AI
          </Typography>
          <Button color="inherit" sx={{ border: "1px solid white", borderRadius: "20px" }}>
            Login
          </Button>
        </Toolbar>
      </AppBar> */}

      {/* Simple Header */}
      <header className="app-header">
        <Typography 
          variant="h3" 
          sx={{ 
            fontWeight: 700, 
            mb: 4,
            textShadow: '2px 2px 6px rgba(0,0,0,0.2)' 
          }}
        >
          SmartCraft AI
        </Typography>
      </header>

      {/* Main Generator Section */}
      <section id="generate">
        <Container maxWidth="md">
          <Paper
            elevation={10}
            sx={{
              p: 5,
              borderRadius: 4,
              background: "rgba(255,255,255,0.15)",
              backdropFilter: "blur(10px)",
              mt: 8,
            }}
          >
            <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold" }}>
              Generate Email Reply
            </Typography>
            <TextField
              fullWidth
              multiline
              rows={6}
              label="Paste your email content"
              variant="outlined"
              value={emailContent}
              onChange={(e) => setEmailContent(e.target.value)}
              sx={{ mb: 3, backgroundColor: "rgba(255,255,255,0.2)", borderRadius: 2 }}
            />

            <FormControl fullWidth sx={{ mb: 3 }}>
              <InputLabel>Tone (Optional)</InputLabel>
              <Select
                value={tone}
                label="Tone (Optional)"
                onChange={(e) => setTone(e.target.value)}
                sx={{ backgroundColor: "rgba(255,255,255,0.2)", borderRadius: 2 }}
              >
                <MenuItem value="">None</MenuItem>
                <MenuItem value="Professional">Professional</MenuItem>
                <MenuItem value="Friendly">Friendly</MenuItem>
                <MenuItem value="Casual">Casual</MenuItem>
                <MenuItem value="Warning">Warning</MenuItem>
              </Select>
            </FormControl>

            <Button
  variant="contained"
  onClick={handleSubmit}
  disabled={!emailContent || loading}
  fullWidth
  sx={{
    mt: 2,
    backgroundColor: "#1e90ff", // bright blue
    '&:hover': { backgroundColor: "#187bcd" } // darker blue on hover
  }}
>
  {loading ? <CircularProgress size={24}/> : "Generate reply"}
</Button>

            {error && (
              <Typography color="error" sx={{ mt: 2 }}>
                {error}
              </Typography>
            )}

            {generatedReply && (
              <Box sx={{ mt: 4 }}>
                <Typography variant="h6" gutterBottom>
                  Generated Reply:
                </Typography>
                <TextField
                  fullWidth
                  multiline
                  rows={6}
                  value={generatedReply}
                  inputProps={{ readOnly: true }}
                  sx={{ backgroundColor: "rgba(255,255,255,0.2)", borderRadius: 2 }}
                />
                <Button 
  variant="outlined" 
  sx={{
    mt: 2,
    color: "#1e90ff",
    borderColor: "#1e90ff",
    '&:hover': {
      backgroundColor: "#1e90ff",
      color: "white"
    }
  }}
  onClick={() => navigator.clipboard.writeText(generatedReply)}
>
  Copy to Clipbord
</Button>
              </Box>
            )}
          </Paper>
        </Container>
      </section>

      {/* Footer */}
      <footer className="footer">
        <Typography variant="body2">
          © {new Date().getFullYear()} SmartCraft AI — Empowering Smarter Communication
        </Typography>
      </footer>
    </div>
  );
}

export default App;




// import { useState } from 'react';
// import './App.css';
// import axios from 'axios'; 
// import {
//   Box,
//   Container,
//   FormControl,
//   InputLabel,
//   MenuItem,
//   TextField,
//   Typography,
//   Select,
//   Button,
//   CircularProgress, 
// } from '@mui/material';


// function App() {
//   const [emailContent, setEmailContent] = useState('');
//   const [tone, setTone] = useState('');
//   const [generatedReply, setGeneratedReply] = useState('');
//   const [loading, setLoading] = useState(false);
//   const [error, setError] = useState('');
  
//   {/*This is going to handle our api request*/}
//   const handleSubmit = async () => {
//      setLoading(true)
//      setError('');
//      try{
//      const response = await axios.post("http://localhost:8080/api/email/generate", {
//       emailContent,
//       tone
//      });
//      setGeneratedReply(typeof response.data == 'string'? response.data: JSON.stringify(response.data));
//      }catch(error){
//       setError("Unable to generate response: Sorry for the inconvenience caused!!");
//      }finally{
//       setLoading(false);
//      }

//   };
//   return (
//     <Container  sx={{ py: 4 }}>
//       <Typography variant="h3" component="h1" gutterBottom>
//         SmartCraft AI
//       </Typography>

//       <Box sx={{ width: '100%', mb: 2 }}>
//         <TextField
//           fullWidth
//           multiline
//           rows={6}
//           variant="outlined"
//           label="Original Email Content"
//           value={emailContent}
//           onChange={(e) => setEmailContent(e.target.value)}
//         />
//       </Box>

//       <Box sx={{ mx: 3, mb: 2 }}>
//         <FormControl fullWidth>
//           <InputLabel>Tone (Optional)</InputLabel>
//           <Select
//             value={tone}
//             label="Tone (Optional)"
//             onChange={(e) => setTone(e.target.value)}
//           >
//             <MenuItem value="">None</MenuItem>
//             <MenuItem value="Professional">Professional</MenuItem>
//             <MenuItem value="Friendly">Friendly</MenuItem>
//             <MenuItem value="Casual">Casual</MenuItem>
//             <MenuItem value="Warning">Warning</MenuItem>
//           </Select>
//         </FormControl>

//         <Button
//         variant='contained'
//         onClick={handleSubmit}
//         disabled={!emailContent || loading}
//         fullWidth>
//         {loading? <CircularProgress size={24}/>: "Generate reply"}

//         </Button>
//       </Box>


//       {error && (
//         <Typography color='error' sx={{mb:2}} gutterBottom>
//         {error}
//       </Typography>

//       )}

//       {
//         generatedReply && (
//          <Box sx={{mt:3}}>
//           <Typography variant='h6' gutterBottom>
//            Generated Reply:
//           </Typography>
//           <TextField fullWidth multiline rows={6} value={generatedReply || ''} inputProps={{readOnly: true}}>
//           </TextField>
//           <Button variant='outlined' sx={{mt:2}}
//           onClick={()=> navigator.clipboard.writeText(generatedReply)}>
//            Copy
//           </Button>
//           </Box>
//         )
//       }
//     </Container>
//   );
// }

// export default App;
