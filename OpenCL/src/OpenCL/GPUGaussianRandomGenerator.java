package OpenCL;
import edu.nyu.class3.montecarlo.random.*;
import org.jocl.*;
import static org.jocl.CL.*;


/** This class uses JOCL to implement a guassian random generator using Box-Muller tansformaton
 *  It transform 2 uniform random vectors into 2 gaussian random vectors */

public class GPUGaussianRandomGenerator {
	// length of uniform and gaussian vectors
	private int _N;

	//firt uniform random vector
	private float[] _uniform1;
	//second uniform random vector
	private float[] _uniform2;
	//first gaussian random vector
	private float[] _gaussian1;
	//second gaussian random vector
	private float[] _gaussian2;

	//constructor
	public GPUGaussianRandomGenerator(float[] uniform1, float[] uniform2) {
		int l1 = uniform1.length;
		int l2 = uniform2.length;
		if(l1 != l2) {
			throw new IllegalArgumentException("the lengths of two uniform vectors need to be same!");
		}
		_N = l1;
		_uniform1 = uniform1;
		_uniform2 = uniform2;
		_gaussian1 = new float[_N];
		_gaussian2 = new float[_N];
		
	}
	
	
	// generate two gaussian random vectors using two uniform vectors, through GPU computing
	public void generates() {
		cl_platform_id[] platforms = new cl_platform_id[1];
		clGetPlatformIDs(1,platforms,null);
		cl_platform_id platform = platforms[0];
		cl_device_id[] devices = new cl_device_id[1];
		clGetDeviceIDs(platform,CL_DEVICE_TYPE_GPU,1,devices,null);
		cl_device_id device = devices[0];

		// Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        //create a context for the selected device
        cl_context context = clCreateContext(contextProperties,1,new cl_device_id[]{device},null,null,null);

        // create command queue for  the selected device
        cl_command_queue commandQueue = clCreateCommandQueue(context,device,0,null);

		//read the program source and compile
		String src = "__kernel void "
        		+ "boxMuller(	__global const float* a, "
        		+ "					__global const float* b, "
        		+ "					__global float* out1, "
        		+ "					__global float* out2) \n"
        		+ "{\n"
        		+ "    int i = get_global_id(0);\n"
        		+ "    out1[i] = sqrt( -2 * log(b[i]) ) "
                + "			* cos( 2 * a[i] * (float)3.14159265359);\n"
                + "    out2[i] = sqrt( -2 * log(b[i]) ) "
                + "			* sin( 2 * a[i] * (float)3.14159265359);\n"
                + "}";
		// create the program from source code
		cl_program program = clCreateProgramWithSource(context,1,new String[]{src},null,null);
		// build the program
		clBuildProgram(program,0,null,null,null,null);
		// create kernel
		cl_kernel kernel = clCreateKernel(program,"boxMuller",null);
		
		//create pointer
		Pointer uniform1 = Pointer.to(_uniform1);
		Pointer uniform2 = Pointer.to(_uniform2);
		Pointer gaussian1 = Pointer.to(_gaussian1);
		Pointer gaussian2 = Pointer.to(_gaussian2);

		// allocate memory for input and output data
		cl_mem memObjects[] = new cl_mem[4];
		memObjects[0] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * _N, uniform1, null);
		memObjects[1] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_float * _N, uniform2, null);
		memObjects[2] = clCreateBuffer(context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_float * _N, null, null);
		memObjects[3] = clCreateBuffer(context,
                CL_MEM_READ_WRITE,
                Sizeof.cl_float * _N, null, null);

		// set arguments for the kernel
		clSetKernelArg(kernel,0,Sizeof.cl_mem,Pointer.to(memObjects[0]));
		clSetKernelArg(kernel,1,Sizeof.cl_mem,Pointer.to(memObjects[1]));
		clSetKernelArg(kernel,2,Sizeof.cl_mem,Pointer.to(memObjects[2]));
		clSetKernelArg(kernel,3,Sizeof.cl_mem,Pointer.to(memObjects[3]));

		//set the work item dimension
		long global_work_size[] = new long[]{_N};
		long local_work_size[] = new long[]{1};

		//execute the kernel
		clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

		//read the output data
		clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0,
                _N * Sizeof.cl_float, gaussian1, 0, null, null);
		clEnqueueReadBuffer(commandQueue, memObjects[3], CL_TRUE, 0,
                _N * Sizeof.cl_float, gaussian2, 0, null, null);
	}

	//geters
	public float[] getGaussianVector1() {
		return _gaussian1;
	}
	public float[] getGaussianVector2() {
		return _gaussian2;
	}
}
