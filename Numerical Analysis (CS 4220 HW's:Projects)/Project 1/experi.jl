"""
    p, R12 = greedy_select(ϕ, X, k)

Compute the leading `k`-by-`N` part of the pivoted Cholesky factorization, i.e.
`ΦXX[p[1:k],p] = R12[:,1:k]'*R12`.  This algorithm should take O(k^2 N) time
and O(kN) space.
"""
function basic_pivchol!(A)
	m, n = size(A)
	if m != n  throw(ArgumentError("Input matrix A should be square"))  end

	# Set up pivot array
	p = zeros(Int, n)
	p[:] .= 1:n

	for j = 1:n

		# Find pivot element
		Apiv, jpiv = findmax(A[l,l] for l=j:n)
		jpiv += j-1
		if Apiv <= 0 throw(ArgumentError("Matrix was not positive definite"))  end

		# Perform column and row swap
		p[j], p[jpiv] = p[jpiv], p[j]
		for i=1:n  A[i,j], A[i,jpiv] = A[i,jpiv], A[i,j]  end
		for i=1:n  A[j,i], A[jpiv,i] = A[jpiv,i], A[j,i]  end

		# Compute a row of R
		A[j,j] = sqrt(Apiv)
		A[j,j+1:n] /= A[j,j]

		# Schur complement update
		for l=j+1:n
			ajl = A[j,l]
			for i=j+1:n
				A[i,l] -= A[j,i]*A[j,l]
			end
		end
	end

	p, UpperTriangular(A)
end

function greedy_select(ϕ, X, k)

	_, N = size(X)
	R = zeros(k,N)
	d = fill(ϕ(0.0), N)
	p = zeros(Int, N)
	p[:] .= 1:N

	for j = 1:k

		pivot_ind = argmax(@view d[j:N]) + j - 1
        # swapsy
		p[j], p[pivot_ind] = p[pivot_ind], p[j]
        d[j], d[pivot_ind] = d[pivot_ind], d[j]

        for i = 1:j-1
            R[i,j], R[i,pivot_ind] = R[i,pivot_ind], R[i,j]
        end

        r_jj = sqrt(d[j])
        R[j, j] = r_jj

        for l = j+1:N
            kval = ϕ(norm(X[:, p[j]] - X[:, p[l]]))
            kval -= dot(@view(R[1:j-1, j]), @view(R[1:j-1, l]))
            R[j, l] = kval / r_jj
            d[l] -= R[j, l]^2
        end
    end
    return p, R
end 
