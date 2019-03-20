package OpenCL;

import java.util.Arrays;
import org.jocl.*;
import static org.jocl.CL.*;

public class GPUPathGenerator {

	// length of each simulation (eg. a year is 252)
	private int _length;
	// daily drift
	private float _r;
	// daily vol
	private float _sigma;
	// initial price
	private float _initial;
	
	//first gaussian vector
	private float[] _gs1;
	//second gaussian vector
	private float[] _gs2;
	// total number of gaussian random number we have
	private int _n;
	// number of gaussian random numbers we have used
	private int _num;
	// maximum number of paths to generate using two gaussian random vectors
	private int _N;
	
	//constructor 
	public GPUPathGenerator(int length, float r, float sigma, float initial, float[] gs1, float[] gs2) {
		_length = length;
		_r = r;
		_sigma = sigma;
		_initial = initial;
		_gs1 = gs1;
		_gs2 = gs2;
		_n = gs1.length;
		_N = (gs1.length+gs2.length)/_length;
		_num = 0;
	}
	
	/** @return float[], the end prices of each path after required length (a year)*/
	public float[] generates() {
		
		// create platform and device
		cl_platform_id[] platforms = new cl_platform_id[1];
		clGetPlatformIDs(1,platforms,null);
		cl_platform_id platform = platforms[0];
		cl_device_id[] devices = new cl_device_id[1];
		clGetDeviceIDs(platform,CL_DEVICE_TYPE_GPU,1,devices,null);
		cl_device_id device = devices[0];
		//Initialize the context property
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
		//create a context for the selected device
		cl_context context  = clCreateContext(contextProperties,1,new cl_device_id[]{device},null,null,null);
		//create commandQueue
		cl_command_queue commandQueue =
                clCreateCommandQueue(context, device, 0, null);
		
		// read program and compile
		String r = "(float)" + Float.toString(_r);
		String sigma = "(float)" + Float.toString(_sigma);
		String src = "__kernel void GBM(__global float* a, __global float* b) \n" +
		            "{\n" +
		            "    int i = get_global_id(0);\n" +
		            "    a[i] = a[i] * exp(" + r +"-" + sigma + "*" +sigma +"/(float)2" + "+"+sigma +"* b[i]);\n" +
		            "}";
		// create the program from source code
		cl_program program = clCreateProgramWithSource(context,1,new String[]{src},null,null);
		//build the program
		clBuildProgram(program,0,null,null,null,null);
		//create the kernel
		cl_kernel kernel = clCreateKernel(program,"GBM",null);
		
		
		
	
		// the price array at the begining, all eaquals to the initial price
		float[] prices = new float[_N];
		Arrays.fill(prices, _initial);
		
		// for every day
		for(int i = 0; i<_length;i++) {
			// load an array of exponential factor for all paths one day
			float[] exponential = new float[_N];
			for (int j=0;j<_N;j++) {
				if(_num < _n) {
					exponential[j] = _gs1[_num];
					_num++;

				}
				else {
					exponential[j] = _gs2[_num - _n];
					_num++;
				}
			}
			
			Pointer srcA = Pointer.to(prices);
			Pointer srcB = Pointer.to(exponential);
			//allocate memory
			cl_mem memObjects[] = new cl_mem[2];
			memObjects[0] = clCreateBuffer(context,
					CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
	                Sizeof.cl_float * _N, srcA, null);
			memObjects[1] = clCreateBuffer(context,
	                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
	                Sizeof.cl_float * _N, srcB, null);
			// set argument to the kernel 
			clSetKernelArg(kernel, 0,
	                Sizeof.cl_mem, Pointer.to(memObjects[0]));
			clSetKernelArg(kernel, 1,
	                Sizeof.cl_mem, Pointer.to(memObjects[1]));
			 // Set the work-item dimensions
	        long global_work_size[] = new long[]{_N};
	        long local_work_size[] = new long[]{1};
	        // Execute the kernel
	        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
	                global_work_size, local_work_size, 0, null, null);
	      //read the output data
			clEnqueueReadBuffer(commandQueue, memObjects[0], CL_TRUE, 0,
	                _N * Sizeof.cl_float, srcA, 0, null, null);
			//System.out.println(prices[6]);
		}
		return prices;
	}	
}
