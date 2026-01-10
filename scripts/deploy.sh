#!/bin/bash

# Financial Ledger Deployment Script
# This script helps deploy the application to Railway and Vercel

set -e

echo "🚀 Financial Ledger Deployment Script"
echo "===================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if required tools are installed
check_dependencies() {
    print_status "Checking dependencies..."

    if ! command -v git &> /dev/null; then
        print_error "Git is not installed. Please install git first."
        exit 1
    fi

    if ! command -v node &> /dev/null; then
        print_error "Node.js is not installed. Please install Node.js first."
        exit 1
    fi

    if ! command -v npm &> /dev/null; then
        print_error "npm is not installed. Please install npm first."
        exit 1
    fi

    print_status "All dependencies are installed."
}

# Check if we're in the project root
check_project_root() {
    if [ ! -d "backend" ] || [ ! -d "frontend" ]; then
        print_error "This script must be run from the project root directory."
        exit 1
    fi
}

# Deploy backend to Railway
deploy_backend() {
    print_status "Deploying backend to Railway..."

    if [ ! -f "deploy/railway/railway.json" ]; then
        print_error "Railway configuration not found. Please ensure deploy/railway/railway.json exists."
        return 1
    fi

    # Copy railway.json to backend directory for deployment
    cp deploy/railway/railway.json backend/

    print_status "Backend prepared for Railway deployment."
    print_warning "Please manually deploy via Railway dashboard:"
    echo "  1. Go to https://railway.app"
    echo "  2. Connect your GitHub repository"
    echo "  3. Set source directory to 'backend/'"
    echo "  4. Add MySQL database plugin"
    echo "  5. Configure environment variables:"
    echo "     - JWT_SECRET"
    echo "     - FRONTEND_URL"

    # Clean up
    rm backend/railway.json
}

# Deploy frontend to Vercel
deploy_frontend() {
    print_status "Deploying frontend to Vercel..."

    if [ ! -f "deploy/vercel/vercel.json" ]; then
        print_error "Vercel configuration not found. Please ensure deploy/vercel/vercel.json exists."
        return 1
    fi

    # Copy vercel.json to frontend directory for deployment
    cp deploy/vercel/vercel.json frontend/

    print_status "Frontend prepared for Vercel deployment."
    print_warning "Please manually deploy via Vercel dashboard:"
    echo "  1. Go to https://vercel.com"
    echo "  2. Connect your GitHub repository"
    echo "  3. Set root directory to 'frontend/'"
    echo "  4. Configure environment variables:"
    echo "     - REACT_APP_API_URL"

    # Clean up
    rm frontend/vercel.json
}

# Main deployment function
main() {
    check_dependencies
    check_project_root

    echo ""
    read -p "Do you want to deploy backend to Railway? (y/n): " deploy_backend_choice
    if [[ $deploy_backend_choice =~ ^[Yy]$ ]]; then
        deploy_backend
    fi

    echo ""
    read -p "Do you want to deploy frontend to Vercel? (y/n): " deploy_frontend_choice
    if [[ $deploy_frontend_choice =~ ^[Yy]$ ]]; then
        deploy_frontend
    fi

    print_status "Deployment preparation complete!"
    print_warning "Remember to:"
    echo "  1. Push your changes to GitHub"
    echo "  2. Complete deployment in Railway/Vercel dashboards"
    echo "  3. Update CORS settings after deployment"
}

# Run main function
main "$@"